package com.smartfarmer.ai.admin.controller;

import com.smartfarmer.ai.admin.dto.AdminUserResponse;
import com.smartfarmer.ai.admin.dto.AuditLogResponse;
import com.smartfarmer.ai.admin.dto.BroadcastNotificationRequest;
import com.smartfarmer.ai.admin.dto.SystemStatsResponse;
import com.smartfarmer.ai.admin.service.AdminService;
import com.smartfarmer.ai.admin.service.AuditLogService;
import com.smartfarmer.ai.common.api.ApiResponse;
import com.smartfarmer.ai.common.api.ApiResponseFactory;
import com.smartfarmer.ai.common.api.PageResponse;
import com.smartfarmer.ai.market.dto.CreateMarketPriceRequest;
import com.smartfarmer.ai.market.dto.CreateMarketRequest;
import com.smartfarmer.ai.market.dto.MarketPriceResponse;
import com.smartfarmer.ai.market.dto.MarketResponse;
import com.smartfarmer.ai.market.service.MarketService;
import com.smartfarmer.ai.scheme.dto.SchemeRequest;
import com.smartfarmer.ai.scheme.dto.SchemeResponse;
import com.smartfarmer.ai.scheme.service.SchemeService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import java.util.UUID;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Admin", description = "Administrative operations, restricted to the ADMIN role")
@RestController
@RequestMapping("/api/v1/admin")
public class AdminController {

    private final AdminService adminService;
    private final AuditLogService auditLogService;
    private final SchemeService schemeService;
    private final MarketService marketService;
    private final ApiResponseFactory apiResponseFactory;

    public AdminController(AdminService adminService,
                           AuditLogService auditLogService,
                           SchemeService schemeService,
                           MarketService marketService,
                           ApiResponseFactory apiResponseFactory) {
        this.adminService = adminService;
        this.auditLogService = auditLogService;
        this.schemeService = schemeService;
        this.marketService = marketService;
        this.apiResponseFactory = apiResponseFactory;
    }

    @GetMapping("/users")
    public ApiResponse<PageResponse<AdminUserResponse>> listUsers(Pageable pageable, HttpServletRequest request) {
        return apiResponseFactory.page("Users retrieved successfully", adminService.listUsers(pageable), request);
    }

    @GetMapping("/users/{id}")
    public ApiResponse<AdminUserResponse> getUserById(@PathVariable UUID id, HttpServletRequest request) {
        return apiResponseFactory.success("User retrieved successfully", adminService.getUserById(id), request);
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
        return apiResponseFactory.success("System stats retrieved successfully", adminService.getStats(), request);
    }

    @GetMapping("/audit-logs")
    public ApiResponse<PageResponse<AuditLogResponse>> getAuditLogs(Pageable pageable, HttpServletRequest request) {
        return apiResponseFactory.page("Audit logs retrieved successfully", auditLogService.list(pageable), request);
    }

    @PostMapping("/schemes")
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<SchemeResponse> createScheme(@Valid @RequestBody SchemeRequest body, HttpServletRequest request) {
        return apiResponseFactory.success("Government scheme created successfully",
                schemeService.createScheme(body), request);
    }

    @PutMapping("/schemes/{id}")
    public ApiResponse<SchemeResponse> updateScheme(@PathVariable UUID id,
                                                    @Valid @RequestBody SchemeRequest body,
                                                    HttpServletRequest request) {
        return apiResponseFactory.success("Government scheme updated successfully",
                schemeService.updateScheme(id, body), request);
    }

    @DeleteMapping("/schemes/{id}")
    public ApiResponse<Void> deleteScheme(@PathVariable UUID id, HttpServletRequest request) {
        schemeService.deleteScheme(id);
        return apiResponseFactory.success("Government scheme deleted successfully", null, request);
    }

    @PostMapping("/markets")
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<MarketResponse> createMarket(@Valid @RequestBody CreateMarketRequest body, HttpServletRequest request) {
        return apiResponseFactory.success("Market created successfully", marketService.createMarket(body), request);
    }

    @PostMapping("/markets/{id}/prices")
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<MarketPriceResponse> addMarketPrice(@PathVariable UUID id,
                                                           @Valid @RequestBody CreateMarketPriceRequest body,
                                                           HttpServletRequest request) {
        return apiResponseFactory.success("Market price recorded successfully",
                marketService.addPrice(id, body), request);
    }

    @DeleteMapping("/market-prices/{priceId}")
    public ApiResponse<Void> deleteMarketPrice(@PathVariable UUID priceId, HttpServletRequest request) {
        marketService.deletePrice(priceId);
        return apiResponseFactory.success("Market price deleted successfully", null, request);
    }

    @PostMapping("/notifications")
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<Integer> sendNotification(@Valid @RequestBody BroadcastNotificationRequest body,
                                                 HttpServletRequest request) {
        int recipients = adminService.sendNotification(body);
        return apiResponseFactory.success("Notification sent successfully", recipients, request);
    }
}
