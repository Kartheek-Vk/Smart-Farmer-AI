package com.smartfarmer.ai.notification.controller;

import com.smartfarmer.ai.common.api.ApiResponse;
import com.smartfarmer.ai.common.api.ApiResponseFactory;
import com.smartfarmer.ai.common.api.PageResponse;
import com.smartfarmer.ai.notification.dto.NotificationResponse;
import com.smartfarmer.ai.notification.service.NotificationService;
import com.smartfarmer.ai.security.CurrentUserService;
import jakarta.servlet.http.HttpServletRequest;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/notifications")
public class NotificationController {

    private final NotificationService notificationService;
    private final CurrentUserService currentUserService;
    private final ApiResponseFactory apiResponseFactory;

    public NotificationController(NotificationService notificationService,
                                  CurrentUserService currentUserService,
                                  ApiResponseFactory apiResponseFactory) {
        this.notificationService = notificationService;
        this.currentUserService = currentUserService;
        this.apiResponseFactory = apiResponseFactory;
    }

    @GetMapping
    public ApiResponse<PageResponse<NotificationResponse>> getNotifications(Pageable pageable, HttpServletRequest request) {
        Page<NotificationResponse> page = notificationService.getNotifications(currentUserService.currentUser(), pageable);
        return apiResponseFactory.page("Notifications retrieved successfully", page, request);
    }

    @GetMapping("/{id}")
    public ApiResponse<NotificationResponse> getNotificationById(@PathVariable UUID id, HttpServletRequest request) {
        NotificationResponse response = notificationService.getNotificationById(id, currentUserService.currentUser());
        return apiResponseFactory.success("Notification retrieved successfully", response, request);
    }

    @PatchMapping("/{id}/read")
    public ApiResponse<Void> markAsRead(@PathVariable UUID id, HttpServletRequest request) {
        notificationService.markAsRead(id, currentUserService.currentUser());
        return apiResponseFactory.success("Notification marked as read", null, request);
    }

    @PatchMapping("/read-all")
    public ApiResponse<Void> markAllAsRead(HttpServletRequest request) {
        notificationService.markAllAsRead(currentUserService.currentUser());
        return apiResponseFactory.success("All notifications marked as read", null, request);
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteNotification(@PathVariable UUID id, HttpServletRequest request) {
        notificationService.deleteNotification(id, currentUserService.currentUser());
        return apiResponseFactory.success("Notification deleted successfully", null, request);
    }
}
