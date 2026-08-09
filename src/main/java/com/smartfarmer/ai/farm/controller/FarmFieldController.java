package com.smartfarmer.ai.farm.controller;

import com.smartfarmer.ai.common.api.ApiResponse;
import com.smartfarmer.ai.common.api.ApiResponseFactory;
import com.smartfarmer.ai.common.api.PageResponse;
import com.smartfarmer.ai.farm.dto.CreateFieldRequest;
import com.smartfarmer.ai.farm.dto.FieldResponse;
import com.smartfarmer.ai.farm.dto.UpdateFieldRequest;
import com.smartfarmer.ai.farm.service.FarmFieldService;
import com.smartfarmer.ai.security.CurrentUserService;
import com.smartfarmer.ai.user.entity.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/farms/{farmId}/fields")
public class FarmFieldController {

    private final FarmFieldService farmFieldService;
    private final CurrentUserService currentUserService;
    private final ApiResponseFactory responseFactory;

    public FarmFieldController(FarmFieldService farmFieldService, CurrentUserService currentUserService, ApiResponseFactory responseFactory) {
        this.farmFieldService = farmFieldService;
        this.currentUserService = currentUserService;
        this.responseFactory = responseFactory;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<FieldResponse> createField(@PathVariable UUID farmId, @Valid @RequestBody CreateFieldRequest request, HttpServletRequest httpRequest) {
        User user = currentUserService.currentUser();
        FieldResponse response = farmFieldService.createField(farmId, request, user);
        return responseFactory.success("Field created successfully", response, httpRequest);
    }

    @GetMapping
    public ApiResponse<PageResponse<FieldResponse>> getFields(@PathVariable UUID farmId, Pageable pageable, HttpServletRequest httpRequest) {
        User user = currentUserService.currentUser();
        Page<FieldResponse> page = farmFieldService.getFields(farmId, user, pageable);
        return responseFactory.page("Fields retrieved successfully", page, httpRequest);
    }

    @GetMapping("/{id}")
    public ApiResponse<FieldResponse> getFieldById(@PathVariable UUID farmId, @PathVariable UUID id, HttpServletRequest httpRequest) {
        User user = currentUserService.currentUser();
        FieldResponse response = farmFieldService.getFieldById(farmId, id, user);
        return responseFactory.success("Field retrieved successfully", response, httpRequest);
    }

    @PutMapping("/{id}")
    public ApiResponse<FieldResponse> updateField(@PathVariable UUID farmId, @PathVariable UUID id, @Valid @RequestBody UpdateFieldRequest request, HttpServletRequest httpRequest) {
        User user = currentUserService.currentUser();
        FieldResponse response = farmFieldService.updateField(farmId, id, request, user);
        return responseFactory.success("Field updated successfully", response, httpRequest);
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteField(@PathVariable UUID farmId, @PathVariable UUID id, HttpServletRequest httpRequest) {
        User user = currentUserService.currentUser();
        farmFieldService.deleteField(farmId, id, user);
        return responseFactory.success("Field deleted successfully", null, httpRequest);
    }
}
