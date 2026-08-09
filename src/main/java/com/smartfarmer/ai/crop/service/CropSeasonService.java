package com.smartfarmer.ai.crop.service;

import com.smartfarmer.ai.common.enums.CropSeasonStatus;
import com.smartfarmer.ai.crop.dto.CreateCropSeasonRequest;
import com.smartfarmer.ai.crop.dto.CropSeasonResponse;
import com.smartfarmer.ai.crop.dto.UpdateCropSeasonRequest;
import com.smartfarmer.ai.crop.entity.Crop;
import com.smartfarmer.ai.crop.entity.CropSeason;
import com.smartfarmer.ai.crop.repository.CropRepository;
import com.smartfarmer.ai.crop.repository.CropSeasonRepository;
import com.smartfarmer.ai.exception.BusinessException;
import com.smartfarmer.ai.exception.ResourceNotFoundException;
import com.smartfarmer.ai.farm.entity.Farm;
import com.smartfarmer.ai.farm.entity.FarmField;
import com.smartfarmer.ai.farm.repository.FarmFieldRepository;
import com.smartfarmer.ai.farm.service.FarmService;
import com.smartfarmer.ai.user.entity.User;
import org.springframework.security.access.AccessDeniedException;
import java.util.Set;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CropSeasonService {

    private static final Set<CropSeasonStatus> HISTORICAL_STATUSES =
            Set.of(CropSeasonStatus.HARVESTED, CropSeasonStatus.FAILED);

    private final CropSeasonRepository cropSeasonRepository;
    private final CropRepository cropRepository;
    private final FarmFieldRepository farmFieldRepository;
    private final FarmService farmService;

    public CropSeasonService(CropSeasonRepository cropSeasonRepository,
                             CropRepository cropRepository,
                             FarmFieldRepository farmFieldRepository,
                             FarmService farmService) {
        this.cropSeasonRepository = cropSeasonRepository;
        this.cropRepository = cropRepository;
        this.farmFieldRepository = farmFieldRepository;
        this.farmService = farmService;
    }

    @Transactional
    public CropSeasonResponse createCropSeason(CreateCropSeasonRequest request, User owner) {
        Farm farm = farmService.getFarmEntityForOwner(request.farmId(), owner.getId());
        Crop crop = cropRepository.findById(request.cropId())
                .orElseThrow(() -> new ResourceNotFoundException("Crop not found with id: " + request.cropId()));

        validateDates(request.startDate(), request.endDate());

        CropSeason season = new CropSeason();
        season.setOwner(owner);
        season.setFarm(farm);
        season.setCrop(crop);
        season.setField(resolveField(request.fieldId(), farm));
        season.setStartDate(request.startDate());
        season.setEndDate(request.endDate());
        season.setStatus(request.status());
        season.setNotes(request.notes());

        return mapToResponse(cropSeasonRepository.save(season));
    }

    @Transactional
    public CropSeasonResponse updateCropSeason(UUID id, UpdateCropSeasonRequest request, User owner) {
        CropSeason season = getOwnedSeason(id, owner);
        validateDates(request.startDate(), request.endDate());
        season.setStartDate(request.startDate());
        season.setEndDate(request.endDate());
        season.setStatus(request.status());
        season.setNotes(request.notes());
        return mapToResponse(cropSeasonRepository.save(season));
    }

    @Transactional(readOnly = true)
    public Page<CropSeasonResponse> getCropSeasons(User owner, UUID farmId, CropSeasonStatus status, Pageable pageable) {
        if (farmId != null) {
            farmService.getFarmEntityForOwner(farmId, owner.getId());
            return cropSeasonRepository.findByOwnerIdAndFarmId(owner.getId(), farmId, pageable).map(this::mapToResponse);
        }
        if (status != null) {
            return cropSeasonRepository.findByOwnerIdAndStatus(owner.getId(), status, pageable).map(this::mapToResponse);
        }
        return cropSeasonRepository.findByOwnerId(owner.getId(), pageable).map(this::mapToResponse);
    }

    @Transactional(readOnly = true)
    public Page<CropSeasonResponse> getCropHistory(User owner, Pageable pageable) {
        return cropSeasonRepository.findByOwnerIdAndStatusIn(owner.getId(), HISTORICAL_STATUSES, pageable)
                .map(this::mapToResponse);
    }

    @Transactional(readOnly = true)
    public CropSeasonResponse getCropSeasonById(UUID id, User owner) {
        return mapToResponse(getOwnedSeason(id, owner));
    }

    @Transactional
    public void deleteCropSeason(UUID id, User owner) {
        cropSeasonRepository.delete(getOwnedSeason(id, owner));
    }

    private CropSeason getOwnedSeason(UUID id, User owner) {
        CropSeason season = cropSeasonRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Crop season not found with id: " + id));
        if (!season.getOwner().getId().equals(owner.getId())) {
            throw new AccessDeniedException("You do not have permission to access this crop season");
        }
        return season;
    }

    private FarmField resolveField(UUID fieldId, Farm farm) {
        if (fieldId == null) {
            return null;
        }
        FarmField field = farmFieldRepository.findById(fieldId)
                .orElseThrow(() -> new ResourceNotFoundException("Field not found with id: " + fieldId));
        if (!field.getFarm().getId().equals(farm.getId())) {
            throw new BusinessException("Field does not belong to the selected farm");
        }
        return field;
    }

    private void validateDates(java.time.LocalDate startDate, java.time.LocalDate endDate) {
        if (endDate != null && endDate.isBefore(startDate)) {
            throw new BusinessException("End date must not be before the start date");
        }
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
                season.getStatus().name(),
                season.getNotes(),
                season.getCreatedAt(),
                season.getUpdatedAt());
    }
}
