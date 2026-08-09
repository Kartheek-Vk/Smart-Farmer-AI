package com.smartfarmer.ai.admin.controller;

import com.smartfarmer.ai.admin.dto.AdminUserResponse;
import com.smartfarmer.ai.admin.dto.AuditLogResponse;
import com.smartfarmer.ai.admin.dto.SystemStatsResponse;
import com.smartfarmer.ai.admin.service.AdminService;
import com.smartfarmer.ai.admin.service.AuditLogService;
import com.smartfarmer.ai.common.api.ApiResponse;
import com.smartfarmer.ai.common.api.ApiResponseFactory;
import com.smartfarmer.ai.common.api.PageResponse;
import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/admin")
public class AdminController {

    private final AdminService adminService;
    private final AuditLogService auditLogService;
    private final ApiResponseFactory apiResponseFactory;

    public AdminController(AdminService adminService,
                           AuditLogService auditLogService,
                           ApiResponseFactory apiResponseFactory) {
        this.adminService = adminService;
        this.auditLogService = auditLogService;
        this.apiResponseFactory = apiResponseFactory;
    }

    @GetMapping("/users")
    public ApiResponse<PageResponse<AdminUserResponse>> listUsers(Pageable pageable, HttpServletRequest request) {
        Page<AdminUserResponse> page = adminService.listUsers(pageable);
        return apiResponseFactory.page("Users retrieved successfully", page, request);
    }

    @GetMapping("/users/{id}")
    public ApiResponse<AdminUserResponse> getUserById(@PathVariable UUID id, HttpServletRequest request) {
        AdminUserResponse response = adminService.getUserById(id);
        return apiResponseFactory.success("User retrieved successfully", response, request);
    }

    @PatchMapping("/users/{id}/activate")
    public ApiResponse<Void> activateUser(@PathVariable UUID id, HttpServletRequest request) {
        adminService.activateUser(id);
        return apiResponseFactory.success("User activated successfully", null, request);
    }

    @PatchMapping("/users/{id}/deactivate")
    public ApiResponse<Void> deactivateUser(@PathVariable UUID id, HttpServletRequest request) {
        adminService.deactivateUser(id);
        return apiResponseFactory.success("User deactivated successfully", null, request);
    }

    @GetMapping("/stats")
    public ApiResponse<SystemStatsResponse> getStats(HttpServletRequest request) {
        SystemStatsResponse stats = adminService.getStats();
        return apiResponseFactory.success("System stats retrieved successfully", stats, request);
    }

    @GetMapping("/audit-logs")
    public ApiResponse<List<AuditLogResponse>> getAuditLogs(HttpServletRequest request) {
        List<AuditLogResponse> logs = auditLogService.latest().stream()
                .map(log -> new AuditLogResponse(
                        log.getId(),
                        log.getActor() != null ? log.getActor().getId() : null,
                        log.getAction(),
                        log.getTargetType(),
                        log.getTargetId(),
                        log.getDetails(),
                        log.getIpAddress(),
                        log.getCreatedAt()
                ))
                .toList();
        return apiResponseFactory.success("Audit logs retrieved successfully", logs, request);
    }
}
