package com.smartfarmer.ai.farmer.controller;

import com.smartfarmer.ai.common.api.ApiResponse;
import com.smartfarmer.ai.common.api.ApiResponseFactory;
import com.smartfarmer.ai.farmer.dto.FarmerProfileResponse;
import com.smartfarmer.ai.farmer.dto.UpdateFarmerProfileRequest;
import com.smartfarmer.ai.farmer.service.FarmerProfileService;
import com.smartfarmer.ai.security.CurrentUserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Farmer profile", description = "Farming specific profile of the authenticated user")
@RestController
@RequestMapping("/api/v1/farmer-profile")
public class FarmerProfileController {

    private final FarmerProfileService farmerProfileService;
    private final CurrentUserService currentUserService;
    private final ApiResponseFactory apiResponseFactory;

    public FarmerProfileController(FarmerProfileService farmerProfileService,
                                   CurrentUserService currentUserService,
                                   ApiResponseFactory apiResponseFactory) {
        this.farmerProfileService = farmerProfileService;
        this.currentUserService = currentUserService;
        this.apiResponseFactory = apiResponseFactory;
    }

    @GetMapping
    public ApiResponse<FarmerProfileResponse> getProfile(HttpServletRequest request) {
        FarmerProfileResponse response = farmerProfileService.getOrCreateProfile(currentUserService.currentUser());
        return apiResponseFactory.success("Farmer profile retrieved successfully", response, request);
    }

    @PutMapping
    public ApiResponse<FarmerProfileResponse> updateProfile(@Valid @RequestBody UpdateFarmerProfileRequest body,
                                                            HttpServletRequest request) {
        FarmerProfileResponse response = farmerProfileService.updateProfile(currentUserService.currentUser(), body);
        return apiResponseFactory.success("Farmer profile updated successfully", response, request);
    }
}
