package com.smartfarmer.ai.crop.controller;

import com.smartfarmer.ai.common.api.ApiResponse;
import com.smartfarmer.ai.common.api.ApiResponseFactory;
import com.smartfarmer.ai.common.api.PageResponse;
import com.smartfarmer.ai.common.enums.CropSeasonStatus;
import com.smartfarmer.ai.crop.dto.CreateCropSeasonRequest;
import com.smartfarmer.ai.crop.dto.CropSeasonResponse;
import com.smartfarmer.ai.crop.dto.UpdateCropSeasonRequest;
import com.smartfarmer.ai.crop.service.CropSeasonService;
import com.smartfarmer.ai.security.CurrentUserService;
import com.smartfarmer.ai.user.entity.User;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Crop seasons", description = "Crops planted on the farms of the authenticated user")
@RestController
@RequestMapping("/api/v1/crop-seasons")
public class CropSeasonController {

    private final CropSeasonService cropSeasonService;
    private final CurrentUserService currentUserService;
    private final ApiResponseFactory responseFactory;

    public CropSeasonController(CropSeasonService cropSeasonService,
                                CurrentUserService currentUserService,
                                ApiResponseFactory responseFactory) {
        this.cropSeasonService = cropSeasonService;
        this.currentUserService = currentUserService;
        this.responseFactory = responseFactory;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<CropSeasonResponse> createCropSeason(@Valid @RequestBody CreateCropSeasonRequest request,
                                                            HttpServletRequest httpRequest) {
        User user = currentUserService.currentUser();
        return responseFactory.success("Crop season created successfully",
                cropSeasonService.createCropSeason(request, user), httpRequest);
    }

    @GetMapping
    public ApiResponse<PageResponse<CropSeasonResponse>> getCropSeasons(
            @RequestParam(required = false) UUID farmId,
            @RequestParam(required = false) CropSeasonStatus status,
            Pageable pageable,
            HttpServletRequest httpRequest) {
        User user = currentUserService.currentUser();
        Page<CropSeasonResponse> page = cropSeasonService.getCropSeasons(user, farmId, status, pageable);
        return responseFactory.page("Crop seasons retrieved successfully", page, httpRequest);
    }

    @GetMapping("/history")
    public ApiResponse<PageResponse<CropSeasonResponse>> getCropHistory(Pageable pageable, HttpServletRequest httpRequest) {
        User user = currentUserService.currentUser();
        Page<CropSeasonResponse> page = cropSeasonService.getCropHistory(user, pageable);
        return responseFactory.page("Crop history retrieved successfully", page, httpRequest);
    }

    @GetMapping("/{id}")
    public ApiResponse<CropSeasonResponse> getCropSeasonById(@PathVariable UUID id, HttpServletRequest httpRequest) {
        User user = currentUserService.currentUser();
        return responseFactory.success("Crop season retrieved successfully",
                cropSeasonService.getCropSeasonById(id, user), httpRequest);
    }

    @PutMapping("/{id}")
    public ApiResponse<CropSeasonResponse> updateCropSeason(@PathVariable UUID id,
                                                            @Valid @RequestBody UpdateCropSeasonRequest request,
                                                            HttpServletRequest httpRequest) {
        User user = currentUserService.currentUser();
        return responseFactory.success("Crop season updated successfully",
                cropSeasonService.updateCropSeason(id, request, user), httpRequest);
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteCropSeason(@PathVariable UUID id, HttpServletRequest httpRequest) {
        User user = currentUserService.currentUser();
        cropSeasonService.deleteCropSeason(id, user);
        return responseFactory.success("Crop season deleted successfully", null, httpRequest);
    }
}
