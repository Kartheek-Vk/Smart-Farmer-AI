package com.smartfarmer.ai.common.api;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

@Component
public class ApiResponseFactory {

    public <T> ApiResponse<T> success(String message, T data, HttpServletRequest request) {
        return ApiResponse.success(message, data, request.getRequestURI());
    }

    public <T> ApiResponse<PageResponse<T>> page(String message, Page<T> page, HttpServletRequest request) {
        return ApiResponse.success(message, new PageResponse<>(
                page.getContent(),
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages()
        ), request.getRequestURI());
    }
}
