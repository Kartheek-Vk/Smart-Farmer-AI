package com.smartfarmer.ai.recommendation.controller;

import com.smartfarmer.ai.common.api.ApiResponse;
import com.smartfarmer.ai.common.api.ApiResponseFactory;
import com.smartfarmer.ai.common.api.PageResponse;
import com.smartfarmer.ai.recommendation.dto.CropRecommendationRequest;
import com.smartfarmer.ai.recommendation.dto.CropRecommendationResponse;
import com.smartfarmer.ai.recommendation.dto.FertilizerRecommendationRequest;
import com.smartfarmer.ai.recommendation.dto.FertilizerRecommendationResponse;
import com.smartfarmer.ai.recommendation.dto.IrrigationRecommendationRequest;
import com.smartfarmer.ai.recommendation.dto.IrrigationRecommendationResponse;
import com.smartfarmer.ai.recommendation.dto.RecommendationHistoryResponse;
import com.smartfarmer.ai.recommendation.service.RecommendationService;
import com.smartfarmer.ai.security.CurrentUserService;
import com.smartfarmer.ai.user.entity.User;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import java.util.UUID;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Recommendations", description = "Crop, fertilizer and irrigation advisory requests")
@RestController
@RequestMapping("/api/v1/recommendations")
public class RecommendationController {

    private final RecommendationService recommendationService;
    private final CurrentUserService currentUserService;
    private final ApiResponseFactory responseFactory;

    public RecommendationController(RecommendationService recommendationService,
                                    CurrentUserService currentUserService,
                                    ApiResponseFactory responseFactory) {
        this.recommendationService = recommendationService;
        this.currentUserService = currentUserService;
        this.responseFactory = responseFactory;
    }

    @PostMapping("/crops")
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<CropRecommendationResponse> createCropRecommendation(
            @Valid @RequestBody CropRecommendationRequest request, HttpServletRequest httpRequest) {
        User user = currentUserService.currentUser();
        return responseFactory.success("Crop recommendation request accepted",
                recommendationService.createCropRecommendation(request, user), httpRequest);
    }

    @GetMapping("/crops")
    public ApiResponse<PageResponse<CropRecommendationResponse>> getCropRecommendations(
            Pageable pageable, HttpServletRequest httpRequest) {
        User user = currentUserService.currentUser();
        return responseFactory.page("Crop recommendations retrieved successfully",
                recommendationService.getCropRecommendations(user, pageable), httpRequest);
    }

    @GetMapping("/crops/{id}")
    public ApiResponse<CropRecommendationResponse> getCropRecommendation(@PathVariable UUID id, HttpServletRequest httpRequest) {
        User user = currentUserService.currentUser();
        return responseFactory.success("Crop recommendation retrieved successfully",
                recommendationService.getCropRecommendation(id, user), httpRequest);
    }

    @PostMapping("/fertilizer")
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<FertilizerRecommendationResponse> createFertilizerRecommendation(
            @Valid @RequestBody FertilizerRecommendationRequest request, HttpServletRequest httpRequest) {
        User user = currentUserService.currentUser();
        return responseFactory.success("Fertilizer recommendation request accepted",
                recommendationService.createFertilizerRecommendation(request, user), httpRequest);
    }

    @GetMapping("/fertilizer")
    public ApiResponse<PageResponse<FertilizerRecommendationResponse>> getFertilizerRecommendations(
            Pageable pageable, HttpServletRequest httpRequest) {
        User user = currentUserService.currentUser();
        return responseFactory.page("Fertilizer recommendations retrieved successfully",
                recommendationService.getFertilizerRecommendations(user, pageable), httpRequest);
    }

    @GetMapping("/fertilizer/{id}")
    public ApiResponse<FertilizerRecommendationResponse> getFertilizerRecommendation(
            @PathVariable UUID id, HttpServletRequest httpRequest) {
        User user = currentUserService.currentUser();
        return responseFactory.success("Fertilizer recommendation retrieved successfully",
                recommendationService.getFertilizerRecommendation(id, user), httpRequest);
    }

    @PostMapping("/irrigation")
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<IrrigationRecommendationResponse> createIrrigationRecommendation(
            @Valid @RequestBody IrrigationRecommendationRequest request, HttpServletRequest httpRequest) {
        User user = currentUserService.currentUser();
        return responseFactory.success("Irrigation recommendation request accepted",
                recommendationService.createIrrigationRecommendation(request, user), httpRequest);
    }

    @GetMapping("/irrigation")
    public ApiResponse<PageResponse<IrrigationRecommendationResponse>> getIrrigationRecommendations(
            Pageable pageable, HttpServletRequest httpRequest) {
        User user = currentUserService.currentUser();
        return responseFactory.page("Irrigation recommendations retrieved successfully",
                recommendationService.getIrrigationRecommendations(user, pageable), httpRequest);
    }

    @GetMapping("/irrigation/{id}")
    public ApiResponse<IrrigationRecommendationResponse> getIrrigationRecommendation(
            @PathVariable UUID id, HttpServletRequest httpRequest) {
        User user = currentUserService.currentUser();
        return responseFactory.success("Irrigation recommendation retrieved successfully",
                recommendationService.getIrrigationRecommendation(id, user), httpRequest);
    }

    @GetMapping("/history")
    public ApiResponse<PageResponse<RecommendationHistoryResponse>> getHistory(Pageable pageable, HttpServletRequest httpRequest) {
        User user = currentUserService.currentUser();
        return responseFactory.page("Recommendation history retrieved successfully",
                recommendationService.getHistory(user, pageable), httpRequest);
    }
}
