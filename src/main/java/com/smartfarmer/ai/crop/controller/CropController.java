package com.smartfarmer.ai.crop.controller;

import com.smartfarmer.ai.common.api.ApiResponse;
import com.smartfarmer.ai.common.api.ApiResponseFactory;
import com.smartfarmer.ai.common.api.PageResponse;
import com.smartfarmer.ai.crop.dto.CropResponse;
import com.smartfarmer.ai.crop.service.CropService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/crops")
public class CropController {

    private final CropService cropService;
    private final ApiResponseFactory responseFactory;

    public CropController(CropService cropService, ApiResponseFactory responseFactory) {
        this.cropService = cropService;
        this.responseFactory = responseFactory;
    }

    @GetMapping
    public ApiResponse<PageResponse<CropResponse>> getCrops(Pageable pageable, HttpServletRequest request) {
        Page<CropResponse> page = cropService.listCrops(pageable);
        return responseFactory.page("Crops retrieved successfully", page, request);
    }

    @GetMapping("/{id}")
    public ApiResponse<CropResponse> getCropById(@PathVariable UUID id, HttpServletRequest request) {
        CropResponse response = cropService.getCropById(id);
        return responseFactory.success("Crop retrieved successfully", response, request);
    }

    @GetMapping("/search")
    public ApiResponse<PageResponse<CropResponse>> searchCrops(@RequestParam(name = "q") String query, Pageable pageable, HttpServletRequest request) {
        Page<CropResponse> page = cropService.searchCrops(query, pageable);
        return responseFactory.page("Crops searched successfully", page, request);
    }
}
