package com.smartfarmer.ai.disease.service;

import com.smartfarmer.ai.common.enums.DiseaseScanStatus;
import com.smartfarmer.ai.common.exception.ResourceNotFoundException;
import com.smartfarmer.ai.common.exception.UnauthorizedException;
import com.smartfarmer.ai.disease.dto.DiseaseAnalysisResponse;
import com.smartfarmer.ai.disease.dto.DiseaseResultResponse;
import com.smartfarmer.ai.disease.dto.DiseaseScanResponse;
import com.smartfarmer.ai.disease.entity.DiseaseResult;
import com.smartfarmer.ai.disease.entity.DiseaseScan;
import com.smartfarmer.ai.disease.repository.DiseaseScanRepository;
import com.smartfarmer.ai.farm.entity.Farm;
import com.smartfarmer.ai.integration.ai.AiServiceClient;
import com.smartfarmer.ai.integration.storage.FileStorageService;
import com.smartfarmer.ai.integration.storage.StoredFile;
import com.smartfarmer.ai.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DiseaseScanService {

    private final DiseaseScanRepository diseaseScanRepository;
    private final FileStorageService fileStorageService;
    private final AiServiceClient aiServiceClient;

    @Transactional
    public DiseaseScanResponse createScan(MultipartFile file, UUID farmId, User user) {
        StoredFile storedFile = fileStorageService.storeFile(file);
        
        DiseaseScan scan = new DiseaseScan();
        scan.setUserId(user.getId());
        scan.setFarmId(farmId);
        scan.setImageUri(storedFile.uri());
        scan.setOriginalFilename(storedFile.originalFilename());
        scan.setContentType(storedFile.contentType());
        scan.setFileSize(storedFile.size());
        scan.setStatus(DiseaseScanStatus.PENDING);
        
        scan = diseaseScanRepository.save(scan);
        
        DiseaseAnalysisResponse aiResponse = aiServiceClient.analyzeDisease(storedFile.uri());
        
        List<DiseaseResult> results = new ArrayList<>();
        if (aiResponse != null && aiResponse.results() != null) {
            for (var aiResult : aiResponse.results()) {
                DiseaseResult result = new DiseaseResult();
                result.setScan(scan);
                result.setDiseaseName(aiResult.diseaseName());
                result.setConfidence(aiResult.confidence());
                result.setSummary(aiResult.summary());
                result.setRecommendation(aiResult.recommendation());
                results.add(result);
            }
        }
        
        scan.setResults(results);
        scan.setStatus(DiseaseScanStatus.COMPLETED);
        scan = diseaseScanRepository.save(scan);
        
        return mapToResponse(scan);
    }

    @Transactional(readOnly = true)
    public Page<DiseaseScanResponse> getScans(User user, Pageable pageable) {
        return diseaseScanRepository.findByUserId(user.getId(), pageable).map(this::mapToResponse);
    }

    @Transactional(readOnly = true)
    public DiseaseScanResponse getScanById(UUID id, User user) {
        DiseaseScan scan = diseaseScanRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Disease scan not found"));
        
        if (!scan.getUserId().equals(user.getId())) {
            throw new UnauthorizedException("You do not have permission to access this scan");
        }
        
        return mapToResponse(scan);
    }

    @Transactional
    public void deleteScan(UUID id, User user) {
        DiseaseScan scan = diseaseScanRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Disease scan not found"));
        
        if (!scan.getUserId().equals(user.getId())) {
            throw new UnauthorizedException("You do not have permission to delete this scan");
        }
        
        fileStorageService.deleteFile(scan.getImageUri());
        diseaseScanRepository.delete(scan);
    }
    
    private DiseaseScanResponse mapToResponse(DiseaseScan scan) {
        List<DiseaseResultResponse> resultResponses = scan.getResults() != null ? 
            scan.getResults().stream().map(r -> new DiseaseResultResponse(
                r.getId(), r.getDiseaseName(), r.getConfidence(), r.getSummary(), r.getRecommendation()
            )).collect(Collectors.toList()) : new ArrayList<>();
            
        return new DiseaseScanResponse(
            scan.getId(),
            scan.getUserId(),
            scan.getFarmId(),
            scan.getImageUri(),
            scan.getOriginalFilename(),
            scan.getContentType(),
            scan.getFileSize(),
            scan.getStatus() != null ? scan.getStatus().name() : null,
            resultResponses,
            scan.getCreatedAt()
        );
    }
}
