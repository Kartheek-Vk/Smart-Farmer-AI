package com.smartfarmer.ai.recommendation.service;

import com.smartfarmer.ai.common.enums.RecommendationStatus;
import com.smartfarmer.ai.common.exception.ResourceNotFoundException;
import com.smartfarmer.ai.common.exception.UnauthorizedException;
import com.smartfarmer.ai.integration.ai.AiServiceClient;
import com.smartfarmer.ai.recommendation.dto.*;
import com.smartfarmer.ai.recommendation.entity.CropRecommendation;
import com.smartfarmer.ai.recommendation.entity.FertilizerRecommendation;
import com.smartfarmer.ai.recommendation.entity.IrrigationRecommendation;
import com.smartfarmer.ai.recommendation.entity.RecommendationHistory;
import com.smartfarmer.ai.recommendation.repository.CropRecommendationRepository;
import com.smartfarmer.ai.recommendation.repository.FertilizerRecommendationRepository;
import com.smartfarmer.ai.recommendation.repository.IrrigationRecommendationRepository;
import com.smartfarmer.ai.recommendation.repository.RecommendationHistoryRepository;
import com.smartfarmer.ai.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RecommendationService {

    private final CropRecommendationRepository cropRepo;
    private final FertilizerRecommendationRepository fertilizerRepo;
    private final IrrigationRecommendationRepository irrigationRepo;
    private final RecommendationHistoryRepository historyRepo;
    private final AiServiceClient aiServiceClient;

    @Transactional
    public CropRecommendationResponse createCropRecommendation(CropRecommendationRequest request, User user) {
        String inputSummary = String.format("Soil: %s, Season: %s, Climate: %s, Irrigation: %s, Info: %s",
                request.soilType(), request.season(), request.climate(), request.irrigationType(), request.additionalInfo());
        
        RecommendationDecision aiResponse = aiServiceClient.getCropRecommendation(request.soilType(), request.season(), request.climate());
        
        CropRecommendation rec = new CropRecommendation();
        rec.setFarmId(request.farmId());
        rec.setUserId(user.getId());
        rec.setSoilType(request.soilType());
        rec.setSeason(request.season());
        rec.setClimate(request.climate());
        rec.setIrrigationType(request.irrigationType());
        rec.setAdditionalInfo(request.additionalInfo());
        rec.setInputSummary(inputSummary);
        rec.setRecommendationText(aiResponse.recommendationText());
        rec.setStatus(RecommendationStatus.COMPLETED);
        
        rec = cropRepo.save(rec);
        saveHistory(user.getId(), "CROP", rec.getId(), inputSummary, rec.getRecommendationText());
        
        return new CropRecommendationResponse(rec.getId(), rec.getFarmId(), rec.getInputSummary(), rec.getRecommendationText(), rec.getStatus().name(), rec.getCreatedAt());
    }

    @Transactional
    public FertilizerRecommendationResponse createFertilizerRecommendation(FertilizerRecommendationRequest request, User user) {
        String inputSummary = String.format("Crop: %s, Soil: %s, pH: %s, N: %s, P: %s, K: %s, Info: %s",
                request.cropName(), request.soilType(), request.soilPh(), request.nitrogen(), request.phosphorus(), request.potassium(), request.additionalInfo());
        
        RecommendationDecision aiResponse = aiServiceClient.getFertilizerRecommendation(request.cropName(), request.soilType(), request.soilPh());
        
        FertilizerRecommendation rec = new FertilizerRecommendation();
        rec.setFarmId(request.farmId());
        rec.setUserId(user.getId());
        rec.setCropName(request.cropName());
        rec.setSoilType(request.soilType());
        rec.setSoilPh(request.soilPh());
        rec.setNitrogen(request.nitrogen());
        rec.setPhosphorus(request.phosphorus());
        rec.setPotassium(request.potassium());
        rec.setAdditionalInfo(request.additionalInfo());
        rec.setInputSummary(inputSummary);
        rec.setRecommendationText(aiResponse.recommendationText());
        rec.setStatus(RecommendationStatus.COMPLETED);
        
        rec = fertilizerRepo.save(rec);
        saveHistory(user.getId(), "FERTILIZER", rec.getId(), inputSummary, rec.getRecommendationText());
        
        return new FertilizerRecommendationResponse(rec.getId(), rec.getFarmId(), rec.getInputSummary(), rec.getRecommendationText(), rec.getStatus().name(), rec.getCreatedAt());
    }

    @Transactional
    public IrrigationRecommendationResponse createIrrigationRecommendation(IrrigationRecommendationRequest request, User user) {
        String inputSummary = String.format("Crop: %s, Soil: %s, Area: %s, Climate: %s, Source: %s, Info: %s",
                request.cropName(), request.soilType(), request.area(), request.climate(), request.waterSource(), request.additionalInfo());
        
        RecommendationDecision aiResponse = aiServiceClient.getIrrigationRecommendation(request.cropName(), request.soilType(), request.climate());
        
        IrrigationRecommendation rec = new IrrigationRecommendation();
        rec.setFarmId(request.farmId());
        rec.setUserId(user.getId());
        rec.setCropName(request.cropName());
        rec.setSoilType(request.soilType());
        rec.setArea(request.area());
        rec.setClimate(request.climate());
        rec.setWaterSource(request.waterSource());
        rec.setAdditionalInfo(request.additionalInfo());
        rec.setInputSummary(inputSummary);
        rec.setRecommendationText(aiResponse.recommendationText());
        rec.setStatus(RecommendationStatus.COMPLETED);
        
        rec = irrigationRepo.save(rec);
        saveHistory(user.getId(), "IRRIGATION", rec.getId(), inputSummary, rec.getRecommendationText());
        
        return new IrrigationRecommendationResponse(rec.getId(), rec.getFarmId(), rec.getInputSummary(), rec.getRecommendationText(), rec.getStatus().name(), rec.getCreatedAt());
    }
    
    @Transactional(readOnly = true)
    public CropRecommendationResponse getCropRecommendation(UUID id, User user) {
        CropRecommendation rec = cropRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Not found"));
        if (!rec.getUserId().equals(user.getId())) throw new UnauthorizedException("Unauthorized");
        return new CropRecommendationResponse(rec.getId(), rec.getFarmId(), rec.getInputSummary(), rec.getRecommendationText(), rec.getStatus().name(), rec.getCreatedAt());
    }
    
    @Transactional(readOnly = true)
    public FertilizerRecommendationResponse getFertilizerRecommendation(UUID id, User user) {
        FertilizerRecommendation rec = fertilizerRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Not found"));
        if (!rec.getUserId().equals(user.getId())) throw new UnauthorizedException("Unauthorized");
        return new FertilizerRecommendationResponse(rec.getId(), rec.getFarmId(), rec.getInputSummary(), rec.getRecommendationText(), rec.getStatus().name(), rec.getCreatedAt());
    }

    @Transactional(readOnly = true)
    public IrrigationRecommendationResponse getIrrigationRecommendation(UUID id, User user) {
        IrrigationRecommendation rec = irrigationRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Not found"));
        if (!rec.getUserId().equals(user.getId())) throw new UnauthorizedException("Unauthorized");
        return new IrrigationRecommendationResponse(rec.getId(), rec.getFarmId(), rec.getInputSummary(), rec.getRecommendationText(), rec.getStatus().name(), rec.getCreatedAt());
    }

    @Transactional(readOnly = true)
    public Page<RecommendationHistory> getHistory(User user, Pageable pageable) {
        return historyRepo.findByUserId(user.getId(), pageable);
    }

    private void saveHistory(UUID userId, String type, UUID referenceId, String inputSummary, String recommendationText) {
        RecommendationHistory history = new RecommendationHistory();
        history.setUserId(userId);
        history.setRecommendationType(type);
        history.setReferenceId(referenceId);
        history.setInputSummary(inputSummary);
        history.setRecommendationText(recommendationText);
        historyRepo.save(history);
    }
}
