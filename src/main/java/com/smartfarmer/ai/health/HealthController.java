package com.smartfarmer.ai.health;

import com.smartfarmer.ai.common.dto.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/health")
public class HealthController {
    @GetMapping
    public ResponseEntity<ApiResponse<Map<String, String>>> health(HttpServletRequest request) {
        Map<String, String> data = Map.of("status", "UP", "service", "Smart Farmer AI Backend");
        return ResponseEntity.ok(ApiResponse.success("Service is healthy", data, request.getRequestURI()));
    }
}
