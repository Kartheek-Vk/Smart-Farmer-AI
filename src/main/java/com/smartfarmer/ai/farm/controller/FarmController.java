package com.smartfarmer.ai.farm.controller;

import com.smartfarmer.ai.common.api.ApiResponse;
import com.smartfarmer.ai.common.api.ApiResponseFactory;
import com.smartfarmer.ai.common.api.PageResponse;
import com.smartfarmer.ai.farm.dto.CreateFarmRequest;
import com.smartfarmer.ai.farm.dto.FarmResponse;
import com.smartfarmer.ai.farm.dto.UpdateFarmRequest;
import com.smartfarmer.ai.farm.service.FarmService;
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
@RequestMapping("/api/v1/farms")
public class FarmController {

    private final FarmService farmService;
    private final CurrentUserService currentUserService;
    private final ApiResponseFactory responseFactory;

    public FarmController(FarmService farmService, CurrentUserService currentUserService, ApiResponseFactory responseFactory) {
        this.farmService = farmService;
        this.currentUserService = currentUserService;
        this.responseFactory = responseFactory;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<FarmResponse> createFarm(@Valid @RequestBody CreateFarmRequest request, HttpServletRequest httpRequest) {
        User user = currentUserService.currentUser();
        FarmResponse response = farmService.createFarm(request, user);
        return responseFactory.success("Farm created successfully", response, httpRequest);
    }

    @GetMapping
    public ApiResponse<PageResponse<FarmResponse>> getFarms(Pageable pageable, HttpServletRequest httpRequest) {
        User user = currentUserService.currentUser();
        Page<FarmResponse> page = farmService.getFarms(user, pageable);
        return responseFactory.page("Farms retrieved successfully", page, httpRequest);
    }

    @GetMapping("/{id}")
    public ApiResponse<FarmResponse> getFarmById(@PathVariable UUID id, HttpServletRequest httpRequest) {
        User user = currentUserService.currentUser();
        FarmResponse response = farmService.getFarmById(id, user);
        return responseFactory.success("Farm retrieved successfully", response, httpRequest);
    }

    @PutMapping("/{id}")
    public ApiResponse<FarmResponse> updateFarm(@PathVariable UUID id, @Valid @RequestBody UpdateFarmRequest request, HttpServletRequest httpRequest) {
        User user = currentUserService.currentUser();
        FarmResponse response = farmService.updateFarm(id, request, user);
        return responseFactory.success("Farm updated successfully", response, httpRequest);
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteFarm(@PathVariable UUID id, HttpServletRequest httpRequest) {
        User user = currentUserService.currentUser();
        farmService.deleteFarm(id, user);
        return responseFactory.success("Farm deleted successfully", null, httpRequest);
    }
}
