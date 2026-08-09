package com.smartfarmer.ai.disease.controller;

import com.smartfarmer.ai.common.api.ApiResponse;
import com.smartfarmer.ai.common.api.ApiResponseFactory;
import com.smartfarmer.ai.common.api.PageResponse;
import com.smartfarmer.ai.disease.dto.DiseaseScanResponse;
import com.smartfarmer.ai.disease.service.DiseaseScanService;
import com.smartfarmer.ai.security.CurrentUserService;
import com.smartfarmer.ai.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/disease-scans")
@RequiredArgsConstructor
public class DiseaseScanController {

    private final DiseaseScanService diseaseScanService;
    private final CurrentUserService currentUserService;
    private final ApiResponseFactory apiResponseFactory;

    @PostMapping
    public ResponseEntity<ApiResponse<DiseaseScanResponse>> createScan(
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "farmId", required = false) UUID farmId) {
        User user = currentUserService.getCurrentUser();
        DiseaseScanResponse response = diseaseScanService.createScan(file, farmId, user);
        return ResponseEntity.ok(apiResponseFactory.success("Disease scan created successfully", response));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<PageResponse<DiseaseScanResponse>>> getScans(Pageable pageable) {
        User user = currentUserService.getCurrentUser();
        Page<DiseaseScanResponse> scans = diseaseScanService.getScans(user, pageable);
        return ResponseEntity.ok(apiResponseFactory.success("Disease scans retrieved successfully", new PageResponse<>(scans)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<DiseaseScanResponse>> getScanById(@PathVariable UUID id) {
        User user = currentUserService.getCurrentUser();
        DiseaseScanResponse response = diseaseScanService.getScanById(id, user);
        return ResponseEntity.ok(apiResponseFactory.success("Disease scan retrieved successfully", response));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteScan(@PathVariable UUID id) {
        User user = currentUserService.getCurrentUser();
        diseaseScanService.deleteScan(id, user);
        return ResponseEntity.ok(apiResponseFactory.success("Disease scan deleted successfully", null));
    }
}
