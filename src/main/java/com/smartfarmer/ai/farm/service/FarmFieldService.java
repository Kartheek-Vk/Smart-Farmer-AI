package com.smartfarmer.ai.farm.service;

import com.smartfarmer.ai.exception.ResourceNotFoundException;
import com.smartfarmer.ai.farm.dto.CreateFieldRequest;
import com.smartfarmer.ai.farm.dto.FieldResponse;
import com.smartfarmer.ai.farm.dto.UpdateFieldRequest;
import com.smartfarmer.ai.farm.entity.Farm;
import com.smartfarmer.ai.farm.entity.FarmField;
import com.smartfarmer.ai.farm.repository.FarmFieldRepository;
import com.smartfarmer.ai.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.UUID;

@Service
public class FarmFieldService {

    private final FarmFieldRepository farmFieldRepository;
    private final FarmService farmService;

    public FarmFieldService(FarmFieldRepository farmFieldRepository, FarmService farmService) {
        this.farmFieldRepository = farmFieldRepository;
        this.farmService = farmService;
    }

    @Transactional
    public FieldResponse createField(UUID farmId, CreateFieldRequest request, User owner) {
        Farm farm = farmService.getFarmEntityForOwner(farmId, owner.getId());
        
        FarmField field = new FarmField();
        field.setFarm(farm);
        field.setName(request.name());
        field.setArea(request.area());
        field.setAreaUnit(request.areaUnit());
        field.setNotes(request.notes());

        FarmField savedField = farmFieldRepository.save(field);
        return mapToResponse(savedField);
    }

    @Transactional(readOnly = true)
    public Page<FieldResponse> getFields(UUID farmId, User owner, Pageable pageable) {
        farmService.getFarmEntityForOwner(farmId, owner.getId());
        return farmFieldRepository.findByFarmId(farmId, pageable).map(this::mapToResponse);
    }

    @Transactional(readOnly = true)
    public FieldResponse getFieldById(UUID farmId, UUID fieldId, User owner) {
        farmService.getFarmEntityForOwner(farmId, owner.getId());
        FarmField field = farmFieldRepository.findById(fieldId)
                .orElseThrow(() -> new ResourceNotFoundException("Field not found with id: " + fieldId));
        if (!field.getFarm().getId().equals(farmId)) {
            throw new ResourceNotFoundException("Field not found in farm");
        }
        return mapToResponse(field);
    }

    @Transactional
    public FieldResponse updateField(UUID farmId, UUID fieldId, UpdateFieldRequest request, User owner) {
        farmService.getFarmEntityForOwner(farmId, owner.getId());
        FarmField field = farmFieldRepository.findById(fieldId)
                .orElseThrow(() -> new ResourceNotFoundException("Field not found with id: " + fieldId));
        if (!field.getFarm().getId().equals(farmId)) {
            throw new ResourceNotFoundException("Field not found in farm");
        }
        
        field.setName(request.name());
        field.setArea(request.area());
        field.setAreaUnit(request.areaUnit());
        field.setNotes(request.notes());

        FarmField updatedField = farmFieldRepository.save(field);
        return mapToResponse(updatedField);
    }

    @Transactional
    public void deleteField(UUID farmId, UUID fieldId, User owner) {
        farmService.getFarmEntityForOwner(farmId, owner.getId());
        FarmField field = farmFieldRepository.findById(fieldId)
                .orElseThrow(() -> new ResourceNotFoundException("Field not found with id: " + fieldId));
        if (!field.getFarm().getId().equals(farmId)) {
            throw new ResourceNotFoundException("Field not found in farm");
        }
        farmFieldRepository.delete(field);
    }

    private FieldResponse mapToResponse(FarmField field) {
        return new FieldResponse(
                field.getId(),
                field.getFarm().getId(),
                field.getName(),
                field.getArea(),
                field.getAreaUnit() != null ? field.getAreaUnit().name() : null,
                field.getNotes(),
                field.getCreatedAt(),
                field.getUpdatedAt()
        );
    }
}
