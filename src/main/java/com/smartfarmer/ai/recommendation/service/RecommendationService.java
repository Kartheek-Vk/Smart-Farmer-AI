package com.smartfarmer.ai.recommendation.service;

import com.smartfarmer.ai.common.enums.RecommendationStatus;
import com.smartfarmer.ai.exception.ResourceNotFoundException;
import com.smartfarmer.ai.farm.entity.Farm;
import com.smartfarmer.ai.farm.service.FarmService;
import com.smartfarmer.ai.integration.ai.AiServiceClient;
import com.smartfarmer.ai.integration.ai.AiServiceException;
import com.smartfarmer.ai.recommendation.dto.CropRecommendationRequest;
import com.smartfarmer.ai.recommendation.dto.CropRecommendationResponse;
import com.smartfarmer.ai.recommendation.dto.FertilizerRecommendationRequest;
import com.smartfarmer.ai.recommendation.dto.FertilizerRecommendationResponse;
import com.smartfarmer.ai.recommendation.dto.IrrigationRecommendationRequest;
import com.smartfarmer.ai.recommendation.dto.IrrigationRecommendationResponse;
import com.smartfarmer.ai.recommendation.dto.RecommendationDecision;
import com.smartfarmer.ai.recommendation.dto.RecommendationHistoryResponse;
import com.smartfarmer.ai.recommendation.entity.CropRecommendation;
import com.smartfarmer.ai.recommendation.entity.FertilizerRecommendation;
import com.smartfarmer.ai.recommendation.entity.IrrigationRecommendation;
import com.smartfarmer.ai.recommendation.entity.RecommendationHistory;
import com.smartfarmer.ai.recommendation.repository.CropRecommendationRepository;
import com.smartfarmer.ai.recommendation.repository.FertilizerRecommendationRepository;
import com.smartfarmer.ai.recommendation.repository.IrrigationRecommendationRepository;
import com.smartfarmer.ai.recommendation.repository.RecommendationHistoryRepository;
import com.smartfarmer.ai.user.entity.User;
import org.springframework.security.access.AccessDeniedException;
import java.util.UUID;
import java.util.function.Function;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Persists recommendation requests and delegates the actual reasoning to the external AI service.
 * When that service is not configured the request is stored with status {@code PENDING} and no
 * recommendation text, so an unanswered request is never presented as a generated result.
 */
@Service
public class RecommendationService {

    private final CropRecommendationRepository cropRepository;
    private final FertilizerRecommendationRepository fertilizerRepository;
    private final IrrigationRecommendationRepository irrigationRepository;
    private final RecommendationHistoryRepository historyRepository;
    private final AiServiceClient aiServiceClient;
    private final FarmService farmService;

    public RecommendationService(CropRecommendationRepository cropRepository,
                                 FertilizerRecommendationRepository fertilizerRepository,
                                 IrrigationRecommendationRepository irrigationRepository,
                                 RecommendationHistoryRepository historyRepository,
                                 AiServiceClient aiServiceClient,
                                 FarmService farmService) {
        this.cropRepository = cropRepository;
        this.fertilizerRepository = fertilizerRepository;
        this.irrigationRepository = irrigationRepository;
        this.historyRepository = historyRepository;
        this.aiServiceClient = aiServiceClient;
        this.farmService = farmService;
    }

    @Transactional
    public CropRecommendationResponse createCropRecommendation(CropRecommendationRequest request, User user) {
        String inputSummary = String.format(
                "Soil: %s, Season: %s, Climate: %s, Irrigation: %s, Info: %s",
                request.soilType(), request.season(), request.climate(), request.irrigationType(), request.additionalInfo());
        RecommendationDecision decision = decide(inputSummary, aiServiceClient::generateCropRecommendation);

        CropRecommendation recommendation = new CropRecommendation();
        recommendation.setUser(user);
        recommendation.setFarm(resolveFarm(request.farmId(), user));
        recommendation.setInputSummary(inputSummary);
        recommendation.setRecommendationText(decision.summary());
        recommendation.setStatus(decision.status());
        recommendation = cropRepository.save(recommendation);

        saveHistory(user, "CROP", recommendation.getId(), inputSummary);
        return toCropResponse(recommendation);
    }

    @Transactional
    public FertilizerRecommendationResponse createFertilizerRecommendation(FertilizerRecommendationRequest request, User user) {
        String inputSummary = String.format(
                "Crop: %s, Soil: %s, pH: %s, N: %s, P: %s, K: %s, Info: %s",
                request.cropName(), request.soilType(), request.soilPh(), request.nitrogen(),
                request.phosphorus(), request.potassium(), request.additionalInfo());
        RecommendationDecision decision = decide(inputSummary, aiServiceClient::generateFertilizerRecommendation);

        FertilizerRecommendation recommendation = new FertilizerRecommendation();
        recommendation.setUser(user);
        recommendation.setFarm(resolveFarm(request.farmId(), user));
        recommendation.setInputSummary(inputSummary);
        recommendation.setRecommendationText(decision.summary());
        recommendation.setStatus(decision.status());
        recommendation = fertilizerRepository.save(recommendation);

        saveHistory(user, "FERTILIZER", recommendation.getId(), inputSummary);
        return toFertilizerResponse(recommendation);
    }

    @Transactional
    public IrrigationRecommendationResponse createIrrigationRecommendation(IrrigationRecommendationRequest request, User user) {
        String inputSummary = String.format(
                "Crop: %s, Soil: %s, Area: %s, Climate: %s, Source: %s, Info: %s",
                request.cropName(), request.soilType(), request.area(), request.climate(),
                request.waterSource(), request.additionalInfo());
        RecommendationDecision decision = decide(inputSummary, aiServiceClient::generateIrrigationRecommendation);

        IrrigationRecommendation recommendation = new IrrigationRecommendation();
        recommendation.setUser(user);
        recommendation.setFarm(resolveFarm(request.farmId(), user));
        recommendation.setInputSummary(inputSummary);
        recommendation.setRecommendationText(decision.summary());
        recommendation.setStatus(decision.status());
        recommendation = irrigationRepository.save(recommendation);

        saveHistory(user, "IRRIGATION", recommendation.getId(), inputSummary);
        return toIrrigationResponse(recommendation);
    }

    @Transactional(readOnly = true)
    public CropRecommendationResponse getCropRecommendation(UUID id, User user) {
        CropRecommendation recommendation = cropRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Crop recommendation not found with id: " + id));
        requireOwner(recommendation.getUser().getId(), user);
        return toCropResponse(recommendation);
    }

    @Transactional(readOnly = true)
    public FertilizerRecommendationResponse getFertilizerRecommendation(UUID id, User user) {
        FertilizerRecommendation recommendation = fertilizerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Fertilizer recommendation not found with id: " + id));
        requireOwner(recommendation.getUser().getId(), user);
        return toFertilizerResponse(recommendation);
    }

    @Transactional(readOnly = true)
    public IrrigationRecommendationResponse getIrrigationRecommendation(UUID id, User user) {
        IrrigationRecommendation recommendation = irrigationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Irrigation recommendation not found with id: " + id));
        requireOwner(recommendation.getUser().getId(), user);
        return toIrrigationResponse(recommendation);
    }

    @Transactional(readOnly = true)
    public Page<CropRecommendationResponse> getCropRecommendations(User user, Pageable pageable) {
        return cropRepository.findByUserId(user.getId(), pageable).map(this::toCropResponse);
    }

    @Transactional(readOnly = true)
    public Page<FertilizerRecommendationResponse> getFertilizerRecommendations(User user, Pageable pageable) {
        return fertilizerRepository.findByUserId(user.getId(), pageable).map(this::toFertilizerResponse);
    }

    @Transactional(readOnly = true)
    public Page<IrrigationRecommendationResponse> getIrrigationRecommendations(User user, Pageable pageable) {
        return irrigationRepository.findByUserId(user.getId(), pageable).map(this::toIrrigationResponse);
    }

    @Transactional(readOnly = true)
    public Page<RecommendationHistoryResponse> getHistory(User user, Pageable pageable) {
        return historyRepository.findByUserId(user.getId(), pageable)
                .map(history -> new RecommendationHistoryResponse(
                        history.getId(),
                        history.getRecommendationType(),
                        history.getReferenceId(),
                        history.getSummary(),
                        history.getCreatedAt()));
    }

    private RecommendationDecision decide(String prompt, Function<String, RecommendationDecision> generator) {
        if (!aiServiceClient.isAvailable()) {
            return new RecommendationDecision(RecommendationStatus.PENDING, null);
        }
        try {
            return generator.apply(prompt);
        } catch (AiServiceException ex) {
            return new RecommendationDecision(RecommendationStatus.FAILED, null);
        }
    }

    private Farm resolveFarm(UUID farmId, User user) {
        return farmId == null ? null : farmService.getFarmEntityForOwner(farmId, user.getId());
    }

    private void requireOwner(UUID ownerId, User user) {
        if (!ownerId.equals(user.getId())) {
            throw new AccessDeniedException("You do not have permission to access this recommendation");
        }
    }

    private void saveHistory(User user, String type, UUID referenceId, String summary) {
        RecommendationHistory history = new RecommendationHistory();
        history.setUser(user);
        history.setRecommendationType(type);
        history.setReferenceId(referenceId.toString());
        history.setSummary(summary);
        historyRepository.save(history);
    }

    private CropRecommendationResponse toCropResponse(CropRecommendation recommendation) {
        return new CropRecommendationResponse(
                recommendation.getId(),
                recommendation.getFarm() != null ? recommendation.getFarm().getId() : null,
                recommendation.getInputSummary(),
                recommendation.getRecommendationText(),
                recommendation.getStatus().name(),
                recommendation.getCreatedAt());
    }

    private FertilizerRecommendationResponse toFertilizerResponse(FertilizerRecommendation recommendation) {
        return new FertilizerRecommendationResponse(
                recommendation.getId(),
                recommendation.getFarm() != null ? recommendation.getFarm().getId() : null,
                recommendation.getInputSummary(),
                recommendation.getRecommendationText(),
                recommendation.getStatus().name(),
                recommendation.getCreatedAt());
    }

    private IrrigationRecommendationResponse toIrrigationResponse(IrrigationRecommendation recommendation) {
        return new IrrigationRecommendationResponse(
                recommendation.getId(),
                recommendation.getFarm() != null ? recommendation.getFarm().getId() : null,
                recommendation.getInputSummary(),
                recommendation.getRecommendationText(),
                recommendation.getStatus().name(),
                recommendation.getCreatedAt());
    }
}
