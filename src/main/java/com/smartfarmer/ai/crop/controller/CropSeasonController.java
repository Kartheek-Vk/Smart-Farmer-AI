package com.smartfarmer.ai.crop.controller;

import com.smartfarmer.ai.common.api.ApiResponse;
import com.smartfarmer.ai.common.api.ApiResponseFactory;
import com.smartfarmer.ai.common.api.PageResponse;
import com.smartfarmer.ai.crop.dto.CreateCropSeasonRequest;
import com.smartfarmer.ai.crop.dto.CropSeasonResponse;
import com.smartfarmer.ai.crop.service.CropSeasonService;
import com.smartfarmer.ai.security.CurrentUserService;
import com.smartfarmer.ai.user.entity.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/crop-seasons")
public class CropSeasonController {

    private final CropSeasonService cropSeasonService;
    private final CurrentUserService currentUserService;
    private final ApiResponseFactory responseFactory;

    public CropSeasonController(CropSeasonService cropSeasonService, CurrentUserService currentUserService, ApiResponseFactory responseFactory) {
        this.cropSeasonService = cropSeasonService;
        this.currentUserService = currentUserService;
        this.responseFactory = responseFactory;
    }

    @PostMapping
    public ApiResponse<CropSeasonResponse> createCropSeason(@Valid @RequestBody CreateCropSeasonRequest request, HttpServletRequest httpRequest) {
        User user = currentUserService.currentUser();
        CropSeasonResponse response = cropSeasonService.createCropSeason(request, user);
        return responseFactory.success("Crop season created successfully", response, httpRequest);
    }

    @GetMapping
    public ApiResponse<PageResponse<CropSeasonResponse>> getCropSeasons(Pageable pageable, HttpServletRequest httpRequest) {
        User user = currentUserService.currentUser();
        Page<CropSeasonResponse> page = cropSeasonService.getCropSeasons(user, pageable);
        return responseFactory.page("Crop seasons retrieved successfully", page, httpRequest);
    }

    @GetMapping("/{id}")
    public ApiResponse<CropSeasonResponse> getCropSeasonById(@PathVariable UUID id, HttpServletRequest httpRequest) {
        User user = currentUserService.currentUser();
        CropSeasonResponse response = cropSeasonService.getCropSeasonById(id, user);
        return responseFactory.success("Crop season retrieved successfully", response, httpRequest);
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteCropSeason(@PathVariable UUID id, HttpServletRequest httpRequest) {
        User user = currentUserService.currentUser();
        cropSeasonService.deleteCropSeason(id, user);
        return responseFactory.success("Crop season deleted successfully", null, httpRequest);
    }
}
