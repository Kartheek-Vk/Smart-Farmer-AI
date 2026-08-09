package com.smartfarmer.ai.crop.service;

import com.smartfarmer.ai.crop.dto.CreateCropSeasonRequest;
import com.smartfarmer.ai.crop.dto.CropSeasonResponse;
import com.smartfarmer.ai.crop.entity.CropSeason;
import com.smartfarmer.ai.crop.repository.CropSeasonRepository;
import com.smartfarmer.ai.crop.service.CropService;
import com.smartfarmer.ai.exception.ResourceNotFoundException;
import com.smartfarmer.ai.exception.UnauthorizedException;
import com.smartfarmer.ai.farm.service.FarmFieldService;
import com.smartfarmer.ai.farm.service.FarmService;
import com.smartfarmer.ai.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.UUID;

@Service
public class CropSeasonService {

    private final CropSeasonRepository cropSeasonRepository;
    private final FarmService farmService;
    private final FarmFieldService farmFieldService;
    private final CropService cropService;

    public CropSeasonService(CropSeasonRepository cropSeasonRepository, FarmService farmService, FarmFieldService farmFieldService, CropService cropService) {
        this.cropSeasonRepository = cropSeasonRepository;
        this.farmService = farmService;
        this.farmFieldService = farmFieldService;
        this.cropService = cropService;
    }

    @Transactional
    public CropSeasonResponse createCropSeason(CreateCropSeasonRequest request, User owner) {
        // Ensure farm belongs to user
        var farm = farmService.getFarmEntityForOwner(request.farmId(), owner.getId());
        
        CropSeason season = new CropSeason();
        season.setOwner(owner);
        season.setFarm(farm);
        
        // This is a minimal implementation relying on existing methods
        // Normally we'd fetch actual entities for Field and Crop, but since the request has IDs we will need those entities.
        throw new UnsupportedOperationException("Incomplete - requires crop/field entity loading");
    }

    @Transactional(readOnly = true)
    public Page<CropSeasonResponse> getCropSeasons(User owner, Pageable pageable) {
        return cropSeasonRepository.findByOwnerId(owner.getId(), pageable).map(this::mapToResponse);
    }

    @Transactional(readOnly = true)
    public CropSeasonResponse getCropSeasonById(UUID id, User owner) {
        CropSeason season = cropSeasonRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("CropSeason not found"));
        if (!season.getOwner().getId().equals(owner.getId())) {
            throw new UnauthorizedException("You do not have permission to access this crop season");
        }
        return mapToResponse(season);
    }

    @Transactional
    public void deleteCropSeason(UUID id, User owner) {
        CropSeason season = cropSeasonRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("CropSeason not found"));
        if (!season.getOwner().getId().equals(owner.getId())) {
            throw new UnauthorizedException("You do not have permission to delete this crop season");
        }
        cropSeasonRepository.delete(season);
    }

    private CropSeasonResponse mapToResponse(CropSeason season) {
        return new CropSeasonResponse(
                season.getId(),
                season.getFarm().getId(),
                season.getField() != null ? season.getField().getId() : null,
                season.getCrop().getId(),
                season.getCrop().getName(),
                season.getStartDate(),
                season.getEndDate(),
                season.getStatus(),
                season.getNotes(),
                season.getCreatedAt(),
                season.getUpdatedAt()
        );
    }
}
