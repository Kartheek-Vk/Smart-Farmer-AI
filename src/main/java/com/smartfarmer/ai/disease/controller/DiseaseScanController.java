package com.smartfarmer.ai.disease.controller;

import com.smartfarmer.ai.common.api.ApiResponse;
import com.smartfarmer.ai.common.api.ApiResponseFactory;
import com.smartfarmer.ai.common.api.PageResponse;
import com.smartfarmer.ai.disease.dto.DiseaseScanResponse;
import com.smartfarmer.ai.disease.service.DiseaseScanService;
import com.smartfarmer.ai.security.CurrentUserService;
import com.smartfarmer.ai.user.entity.User;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "Disease scans", description = "Leaf image uploads and their analysis status")
@RestController
@RequestMapping("/api/v1/disease-scans")
public class DiseaseScanController {

    private final DiseaseScanService diseaseScanService;
    private final CurrentUserService currentUserService;
    private final ApiResponseFactory responseFactory;

    public DiseaseScanController(DiseaseScanService diseaseScanService,
                                 CurrentUserService currentUserService,
                                 ApiResponseFactory responseFactory) {
        this.diseaseScanService = diseaseScanService;
        this.currentUserService = currentUserService;
        this.responseFactory = responseFactory;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<DiseaseScanResponse> createScan(@RequestParam("file") MultipartFile file,
                                                       @RequestParam(value = "farmId", required = false) UUID farmId,
                                                       HttpServletRequest request) {
        User user = currentUserService.currentUser();
        return responseFactory.success("Disease scan created successfully",
                diseaseScanService.createScan(file, farmId, user), request);
    }

    @GetMapping
    public ApiResponse<PageResponse<DiseaseScanResponse>> getScans(@RequestParam(required = false) UUID farmId,
                                                                   Pageable pageable,
                                                                   HttpServletRequest request) {
        User user = currentUserService.currentUser();
        Page<DiseaseScanResponse> page = diseaseScanService.getScans(user, farmId, pageable);
        return responseFactory.page("Disease scans retrieved successfully", page, request);
    }

    @GetMapping("/{id}")
    public ApiResponse<DiseaseScanResponse> getScanById(@PathVariable UUID id, HttpServletRequest request) {
        User user = currentUserService.currentUser();
        return responseFactory.success("Disease scan retrieved successfully",
                diseaseScanService.getScanById(id, user), request);
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteScan(@PathVariable UUID id, HttpServletRequest request) {
        diseaseScanService.deleteScan(id, currentUserService.currentUser());
        return responseFactory.success("Disease scan deleted successfully", null, request);
    }
}
