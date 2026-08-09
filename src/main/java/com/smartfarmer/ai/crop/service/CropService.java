package com.smartfarmer.ai.crop.service;

import com.smartfarmer.ai.crop.dto.CropResponse;
import com.smartfarmer.ai.crop.entity.Crop;
import com.smartfarmer.ai.crop.repository.CropRepository;
import com.smartfarmer.ai.exception.ResourceNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.UUID;

@Service
public class CropService {

    private final CropRepository cropRepository;

    public CropService(CropRepository cropRepository) {
        this.cropRepository = cropRepository;
    }

    @Transactional(readOnly = true)
    public Page<CropResponse> listCrops(Pageable pageable) {
        return cropRepository.findAll(pageable).map(this::mapToResponse);
    }

    @Transactional(readOnly = true)
    public CropResponse getCropById(UUID id) {
        Crop crop = cropRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Crop not found with id: " + id));
        return mapToResponse(crop);
    }

    @Transactional(readOnly = true)
    public Page<CropResponse> searchCrops(String query, Pageable pageable) {
        return cropRepository.findByNameContainingIgnoreCase(query, pageable).map(this::mapToResponse);
    }

    private CropResponse mapToResponse(Crop crop) {
        return new CropResponse(
                crop.getId(),
                crop.getName(),
                crop.getCategory(),
                crop.getDescription(),
                crop.getSeason()
        );
    }
}
