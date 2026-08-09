package com.smartfarmer.ai.scheme.controller;

import com.smartfarmer.ai.common.api.ApiResponse;
import com.smartfarmer.ai.common.api.ApiResponseFactory;
import com.smartfarmer.ai.common.api.PageResponse;
import com.smartfarmer.ai.scheme.dto.SchemeResponse;
import com.smartfarmer.ai.scheme.service.SchemeService;
import jakarta.servlet.http.HttpServletRequest;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/schemes")
public class SchemeController {

    private final SchemeService schemeService;
    private final ApiResponseFactory apiResponseFactory;

    public SchemeController(SchemeService schemeService, ApiResponseFactory apiResponseFactory) {
        this.schemeService = schemeService;
        this.apiResponseFactory = apiResponseFactory;
    }

    @GetMapping
    public ApiResponse<PageResponse<SchemeResponse>> listSchemes(
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String state,
            @RequestParam(required = false) Boolean active,
            Pageable pageable,
            HttpServletRequest request) {
        Page<SchemeResponse> page = schemeService.listSchemes(category, state, active, pageable);
        return apiResponseFactory.page("Government schemes retrieved successfully", page, request);
    }

    @GetMapping("/{id}")
    public ApiResponse<SchemeResponse> getSchemeById(@PathVariable UUID id, HttpServletRequest request) {
        SchemeResponse response = schemeService.getSchemeById(id);
        return apiResponseFactory.success("Government scheme retrieved successfully", response, request);
    }

    @GetMapping("/search")
    public ApiResponse<PageResponse<SchemeResponse>> searchSchemes(
            @RequestParam String q,
            Pageable pageable,
            HttpServletRequest request) {
        Page<SchemeResponse> page = schemeService.searchSchemes(q, pageable);
        return apiResponseFactory.page("Search results retrieved successfully", page, request);
    }
}
