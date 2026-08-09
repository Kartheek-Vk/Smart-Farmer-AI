package com.smartfarmer.ai.report.controller;

import com.smartfarmer.ai.common.api.ApiResponse;
import com.smartfarmer.ai.common.api.ApiResponseFactory;
import com.smartfarmer.ai.common.api.PageResponse;
import com.smartfarmer.ai.report.dto.ReportResponse;
import com.smartfarmer.ai.report.service.ReportService;
import com.smartfarmer.ai.security.CurrentUserService;
import jakarta.servlet.http.HttpServletRequest;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/reports")
public class ReportController {

    private final ReportService reportService;
    private final CurrentUserService currentUserService;
    private final ApiResponseFactory apiResponseFactory;

    public ReportController(ReportService reportService,
                            CurrentUserService currentUserService,
                            ApiResponseFactory apiResponseFactory) {
        this.reportService = reportService;
        this.currentUserService = currentUserService;
        this.apiResponseFactory = apiResponseFactory;
    }

    @GetMapping
    public ApiResponse<PageResponse<ReportResponse>> listReports(Pageable pageable, HttpServletRequest request) {
        Page<ReportResponse> page = reportService.listReports(currentUserService.currentUser(), pageable);
        return apiResponseFactory.page("Reports retrieved successfully", page, request);
    }

    @GetMapping("/{id}")
    public ApiResponse<ReportResponse> getReportById(@PathVariable UUID id, HttpServletRequest request) {
        ReportResponse response = reportService.getReportById(id, currentUserService.currentUser());
        return apiResponseFactory.success("Report retrieved successfully", response, request);
    }
}
