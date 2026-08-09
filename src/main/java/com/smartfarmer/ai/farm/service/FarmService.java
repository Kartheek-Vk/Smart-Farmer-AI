package com.smartfarmer.ai.farm.service;

import com.smartfarmer.ai.exception.ResourceNotFoundException;
import com.smartfarmer.ai.farm.dto.CreateFarmRequest;
import com.smartfarmer.ai.farm.dto.FarmResponse;
import com.smartfarmer.ai.farm.dto.UpdateFarmRequest;
import com.smartfarmer.ai.farm.entity.Farm;
import com.smartfarmer.ai.farm.repository.FarmRepository;
import com.smartfarmer.ai.user.entity.User;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.UUID;

@Service
public class FarmService {

    private final FarmRepository farmRepository;

    public FarmService(FarmRepository farmRepository) {
        this.farmRepository = farmRepository;
    }

    @Transactional
    public FarmResponse createFarm(CreateFarmRequest request, User owner) {
        Farm farm = new Farm();
        farm.setOwner(owner);
        farm.setName(request.name());
        farm.setLocation(request.location());
        farm.setLatitude(request.latitude());
        farm.setLongitude(request.longitude());
        farm.setArea(request.area());
        farm.setAreaUnit(request.areaUnit());
        farm.setSoilType(request.soilType());
        farm.setIrrigationType(request.irrigationType());
        farm.setOwnershipType(request.ownershipType());

        Farm savedFarm = farmRepository.save(farm);
        return mapToResponse(savedFarm);
    }

    @Transactional(readOnly = true)
    public Page<FarmResponse> getFarms(User owner, Pageable pageable) {
        return farmRepository.findByOwnerId(owner.getId(), pageable).map(this::mapToResponse);
    }

    @Transactional(readOnly = true)
    public FarmResponse getFarmById(UUID id, User owner) {
        Farm farm = getFarmEntityForOwner(id, owner.getId());
        return mapToResponse(farm);
    }

    @Transactional
    public FarmResponse updateFarm(UUID id, UpdateFarmRequest request, User owner) {
        Farm farm = getFarmEntityForOwner(id, owner.getId());
        
        farm.setName(request.name());
        farm.setLocation(request.location());
        farm.setLatitude(request.latitude());
        farm.setLongitude(request.longitude());
        farm.setArea(request.area());
        farm.setAreaUnit(request.areaUnit());
        farm.setSoilType(request.soilType());
        farm.setIrrigationType(request.irrigationType());
        farm.setOwnershipType(request.ownershipType());

        Farm updatedFarm = farmRepository.save(farm);
        return mapToResponse(updatedFarm);
    }

    @Transactional
    public void deleteFarm(UUID id, User owner) {
        Farm farm = getFarmEntityForOwner(id, owner.getId());
        farmRepository.delete(farm);
    }

    public Farm getFarmEntityForOwner(UUID farmId, UUID ownerId) {
        Farm farm = farmRepository.findById(farmId)
                .orElseThrow(() -> new ResourceNotFoundException("Farm not found with id: " + farmId));
        if (!farm.getOwner().getId().equals(ownerId)) {
            throw new AccessDeniedException("You do not have permission to access this farm");
        }
        return farm;
    }

    private FarmResponse mapToResponse(Farm farm) {
        return new FarmResponse(
                farm.getId(),
                farm.getName(),
                farm.getLocation(),
                farm.getLatitude(),
                farm.getLongitude(),
                farm.getArea(),
                farm.getAreaUnit() != null ? farm.getAreaUnit().name() : null,
                farm.getSoilType() != null ? farm.getSoilType().name() : null,
                farm.getIrrigationType() != null ? farm.getIrrigationType().name() : null,
                farm.getOwnershipType() != null ? farm.getOwnershipType().name() : null,
                farm.getCreatedAt(),
                farm.getUpdatedAt()
        );
    }
}
