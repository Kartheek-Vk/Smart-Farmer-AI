package com.smartfarmer.ai.common.api;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Component
public class ApiResponseFactory {

    public <T> ApiResponse<T> success(String message, T data, HttpServletRequest request) {
        return ApiResponse.success(message, data, request.getRequestURI());
    }

    public <T> ApiResponse<T> success(String message, T data) {
        return ApiResponse.success(message, data, currentRequestUri());
    }

    public <T> ApiResponse<PageResponse<T>> page(String message, Page<T> page, HttpServletRequest request) {
        return ApiResponse.success(message, PageResponse.from(page), request.getRequestURI());
    }

    public <T> ApiResponse<PageResponse<T>> page(String message, Page<T> page) {
        return ApiResponse.success(message, PageResponse.from(page), currentRequestUri());
    }

    private String currentRequestUri() {
        if (RequestContextHolder.getRequestAttributes() instanceof ServletRequestAttributes attributes) {
            return attributes.getRequest().getRequestURI();
        }
        return null;
    }
}
