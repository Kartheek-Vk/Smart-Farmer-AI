package com.smartfarmer.ai.disease.service;

import com.smartfarmer.ai.common.enums.DiseaseScanStatus;
import com.smartfarmer.ai.disease.dto.DiseaseAnalysisResponse;
import com.smartfarmer.ai.disease.dto.DiseaseResultResponse;
import com.smartfarmer.ai.disease.dto.DiseaseScanResponse;
import com.smartfarmer.ai.disease.entity.DiseaseResult;
import com.smartfarmer.ai.disease.entity.DiseaseScan;
import com.smartfarmer.ai.disease.repository.DiseaseResultRepository;
import com.smartfarmer.ai.disease.repository.DiseaseScanRepository;
import com.smartfarmer.ai.exception.BusinessException;
import com.smartfarmer.ai.exception.ResourceNotFoundException;
import com.smartfarmer.ai.farm.entity.Farm;
import com.smartfarmer.ai.farm.service.FarmService;
import com.smartfarmer.ai.integration.ai.AiServiceClient;
import com.smartfarmer.ai.integration.ai.AiServiceException;
import com.smartfarmer.ai.integration.storage.FileStorageService;
import com.smartfarmer.ai.integration.storage.StoredFile;
import com.smartfarmer.ai.user.entity.User;
import org.springframework.security.access.AccessDeniedException;
import java.io.IOException;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
public class DiseaseScanService {

    private final DiseaseScanRepository diseaseScanRepository;
    private final DiseaseResultRepository diseaseResultRepository;
    private final FileStorageService fileStorageService;
    private final AiServiceClient aiServiceClient;
    private final FarmService farmService;

    public DiseaseScanService(DiseaseScanRepository diseaseScanRepository,
                              DiseaseResultRepository diseaseResultRepository,
                              FileStorageService fileStorageService,
                              AiServiceClient aiServiceClient,
                              FarmService farmService) {
        this.diseaseScanRepository = diseaseScanRepository;
        this.diseaseResultRepository = diseaseResultRepository;
        this.fileStorageService = fileStorageService;
        this.aiServiceClient = aiServiceClient;
        this.farmService = farmService;
    }

    @Transactional
    public DiseaseScanResponse createScan(MultipartFile file, UUID farmId, User user) {
        Farm farm = farmId == null ? null : farmService.getFarmEntityForOwner(farmId, user.getId());
        byte[] content = readBytes(file);
        StoredFile storedFile = fileStorageService.store(file);

        DiseaseScan scan = new DiseaseScan();
        scan.setUser(user);
        scan.setFarm(farm);
        scan.setImageStorageKey(storedFile.storageKey());
        scan.setImageUri(storedFile.uri());
        scan.setOriginalFilename(storedFile.originalFilename());
        scan.setContentType(storedFile.contentType());
        scan.setFileSize(storedFile.size());
        scan.setStatus(DiseaseScanStatus.PENDING);
        scan = diseaseScanRepository.save(scan);

        List<DiseaseResult> results = analyze(scan, content, storedFile);
        return mapToResponse(scan, results);
    }

    @Transactional(readOnly = true)
    public Page<DiseaseScanResponse> getScans(User user, UUID farmId, Pageable pageable) {
        Page<DiseaseScan> scans = farmId == null
                ? diseaseScanRepository.findByUserId(user.getId(), pageable)
                : diseaseScanRepository.findByUserIdAndFarmId(user.getId(), farmId, pageable);
        return scans.map(scan -> mapToResponse(scan, diseaseResultRepository.findByDiseaseScanId(scan.getId())));
    }

    @Transactional(readOnly = true)
    public DiseaseScanResponse getScanById(UUID id, User user) {
        DiseaseScan scan = getOwnedScan(id, user);
        return mapToResponse(scan, diseaseResultRepository.findByDiseaseScanId(scan.getId()));
    }

    @Transactional
    public void deleteScan(UUID id, User user) {
        DiseaseScan scan = getOwnedScan(id, user);
        diseaseResultRepository.deleteAll(diseaseResultRepository.findByDiseaseScanId(scan.getId()));
        diseaseScanRepository.delete(scan);
        fileStorageService.delete(scan.getImageStorageKey());
    }

    /**
     * Sends the image to the external AI service. When no provider is configured the scan stays
     * {@code PENDING} so the caller can tell that no analysis has happened yet.
     */
    private List<DiseaseResult> analyze(DiseaseScan scan, byte[] content, StoredFile storedFile) {
        if (!aiServiceClient.isAvailable()) {
            return List.of();
        }
        scan.setStatus(DiseaseScanStatus.PROCESSING);
        diseaseScanRepository.save(scan);
        try {
            DiseaseAnalysisResponse analysis =
                    aiServiceClient.analyzeDisease(content, storedFile.contentType(), storedFile.originalFilename());
            DiseaseResult result = new DiseaseResult();
            result.setDiseaseScan(scan);
            result.setDiseaseName(analysis.diseaseName());
            result.setConfidence(analysis.confidence());
            result.setSummary(analysis.summary());
            result.setRecommendation(analysis.recommendation());
            List<DiseaseResult> results = List.of(diseaseResultRepository.save(result));
            scan.setStatus(DiseaseScanStatus.COMPLETED);
            diseaseScanRepository.save(scan);
            return results;
        } catch (AiServiceException ex) {
            scan.setStatus(DiseaseScanStatus.FAILED);
            diseaseScanRepository.save(scan);
            return List.of();
        }
    }

    private DiseaseScan getOwnedScan(UUID id, User user) {
        DiseaseScan scan = diseaseScanRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Disease scan not found with id: " + id));
        if (!scan.getUser().getId().equals(user.getId())) {
            throw new AccessDeniedException("You do not have permission to access this scan");
        }
        return scan;
    }

    private byte[] readBytes(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new BusinessException("File is required");
        }
        try {
            return file.getBytes();
        } catch (IOException ex) {
            throw new BusinessException("Unable to read the uploaded file");
        }
    }

    private DiseaseScanResponse mapToResponse(DiseaseScan scan, List<DiseaseResult> results) {
        List<DiseaseResultResponse> resultResponses = results.stream()
                .map(result -> new DiseaseResultResponse(
                        result.getId(),
                        result.getDiseaseName(),
                        result.getConfidence(),
                        result.getSummary(),
                        result.getRecommendation()))
                .toList();
        return new DiseaseScanResponse(
                scan.getId(),
                scan.getUser().getId(),
                scan.getFarm() != null ? scan.getFarm().getId() : null,
                scan.getImageUri(),
                scan.getOriginalFilename(),
                scan.getContentType(),
                scan.getFileSize(),
                scan.getStatus().name(),
                resultResponses,
                scan.getCreatedAt());
    }
}
