package com.smartfarmer.ai.recommendation.controller;

import com.smartfarmer.ai.common.api.ApiResponse;
import com.smartfarmer.ai.common.api.ApiResponseFactory;
import com.smartfarmer.ai.common.api.PageResponse;
import com.smartfarmer.ai.recommendation.dto.*;
import com.smartfarmer.ai.recommendation.entity.RecommendationHistory;
import com.smartfarmer.ai.recommendation.service.RecommendationService;
import com.smartfarmer.ai.security.CurrentUserService;
import com.smartfarmer.ai.user.entity.User;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/recommendations")
@RequiredArgsConstructor
public class RecommendationController {

    private final RecommendationService recommendationService;
    private final CurrentUserService currentUserService;
    private final ApiResponseFactory apiResponseFactory;

    @PostMapping("/crops")
    public ResponseEntity<ApiResponse<CropRecommendationResponse>> createCropRecommendation(@Valid @RequestBody CropRecommendationRequest request) {
        User user = currentUserService.getCurrentUser();
        CropRecommendationResponse response = recommendationService.createCropRecommendation(request, user);
        return ResponseEntity.ok(apiResponseFactory.success("Crop recommendation generated", response));
    }
    
    @GetMapping("/crops/{id}")
    public ResponseEntity<ApiResponse<CropRecommendationResponse>> getCropRecommendation(@PathVariable UUID id) {
        User user = currentUserService.getCurrentUser();
        CropRecommendationResponse response = recommendationService.getCropRecommendation(id, user);
        return ResponseEntity.ok(apiResponseFactory.success("Crop recommendation retrieved", response));
    }

    @PostMapping("/fertilizer")
    public ResponseEntity<ApiResponse<FertilizerRecommendationResponse>> createFertilizerRecommendation(@Valid @RequestBody FertilizerRecommendationRequest request) {
        User user = currentUserService.getCurrentUser();
        FertilizerRecommendationResponse response = recommendationService.createFertilizerRecommendation(request, user);
        return ResponseEntity.ok(apiResponseFactory.success("Fertilizer recommendation generated", response));
    }
    
    @GetMapping("/fertilizer/{id}")
    public ResponseEntity<ApiResponse<FertilizerRecommendationResponse>> getFertilizerRecommendation(@PathVariable UUID id) {
        User user = currentUserService.getCurrentUser();
        FertilizerRecommendationResponse response = recommendationService.getFertilizerRecommendation(id, user);
        return ResponseEntity.ok(apiResponseFactory.success("Fertilizer recommendation retrieved", response));
    }

    @PostMapping("/irrigation")
    public ResponseEntity<ApiResponse<IrrigationRecommendationResponse>> createIrrigationRecommendation(@Valid @RequestBody IrrigationRecommendationRequest request) {
        User user = currentUserService.getCurrentUser();
        IrrigationRecommendationResponse response = recommendationService.createIrrigationRecommendation(request, user);
        return ResponseEntity.ok(apiResponseFactory.success("Irrigation recommendation generated", response));
    }
    
    @GetMapping("/irrigation/{id}")
    public ResponseEntity<ApiResponse<IrrigationRecommendationResponse>> getIrrigationRecommendation(@PathVariable UUID id) {
        User user = currentUserService.getCurrentUser();
        IrrigationRecommendationResponse response = recommendationService.getIrrigationRecommendation(id, user);
        return ResponseEntity.ok(apiResponseFactory.success("Irrigation recommendation retrieved", response));
    }

    @GetMapping("/history")
    public ResponseEntity<ApiResponse<PageResponse<RecommendationHistory>>> getHistory(Pageable pageable) {
        User user = currentUserService.getCurrentUser();
        Page<RecommendationHistory> history = recommendationService.getHistory(user, pageable);
        return ResponseEntity.ok(apiResponseFactory.success("History retrieved", new PageResponse<>(history)));
    }
}
