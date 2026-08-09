package com.smartfarmer.ai.authentication.controller;

import com.smartfarmer.ai.authentication.dto.*;
import com.smartfarmer.ai.authentication.service.AuthService;
import com.smartfarmer.ai.common.dto.ApiResponse;
import com.smartfarmer.ai.common.dto.ApiResponseFactory;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthService authService;
    private final ApiResponseFactory apiResponseFactory;

    public AuthController(AuthService authService, ApiResponseFactory apiResponseFactory) {
        this.authService = authService;
        this.apiResponseFactory = apiResponseFactory;
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<AuthResponse>> register(@Valid @RequestBody RegisterRequest request, HttpServletRequest httpRequest) {
        AuthResponse response = authService.register(request);
        return ResponseEntity.ok(ApiResponse.success("User registered successfully", response, httpRequest.getRequestURI()));
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthResponse>> login(@Valid @RequestBody LoginRequest request, HttpServletRequest httpRequest) {
        AuthResponse response = authService.login(request);
        return ResponseEntity.ok(ApiResponse.success("User logged in successfully", response, httpRequest.getRequestURI()));
    }

    @PostMapping("/refresh")
    public ResponseEntity<ApiResponse<AuthResponse>> refresh(@Valid @RequestBody RefreshTokenRequest request, HttpServletRequest httpRequest) {
        AuthResponse response = authService.refresh(request);
        return ResponseEntity.ok(ApiResponse.success("Token refreshed successfully", response, httpRequest.getRequestURI()));
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<Void>> logout(@Valid @RequestBody RefreshTokenRequest request, HttpServletRequest httpRequest) {
        authService.logout(request);
        return ResponseEntity.ok(ApiResponse.success("Logged out successfully", null, httpRequest.getRequestURI()));
    }

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<AuthUserResponse>> getCurrentUser(HttpServletRequest httpRequest) {
        AuthUserResponse response = authService.getCurrentUser();
        return ResponseEntity.ok(ApiResponse.success("Current user retrieved successfully", response, httpRequest.getRequestURI()));
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<ApiResponse<OtpResponse>> forgotPassword(@Valid @RequestBody OtpRequest request, HttpServletRequest httpRequest) {
        OtpResponse response = authService.forgotPassword(request);
        return ResponseEntity.ok(ApiResponse.success("Forgot password OTP generated", response, httpRequest.getRequestURI()));
    }

    @PostMapping("/reset-password")
    public ResponseEntity<ApiResponse<Void>> resetPassword(@Valid @RequestBody OtpVerificationRequest request, HttpServletRequest httpRequest) {
        authService.resetPassword(request);
        return ResponseEntity.ok(ApiResponse.success("Password reset successfully", null, httpRequest.getRequestURI()));
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<ApiResponse<OtpResponse>> verifyOtp(@Valid @RequestBody OtpVerificationRequest request, HttpServletRequest httpRequest) {
        OtpResponse response = authService.verifyOtp(request);
        return ResponseEntity.ok(ApiResponse.success("OTP verified successfully", response, httpRequest.getRequestURI()));
    }

    @PostMapping("/resend-otp")
    public ResponseEntity<ApiResponse<OtpResponse>> resendOtp(@Valid @RequestBody OtpRequest request, HttpServletRequest httpRequest) {
        OtpResponse response = authService.resendOtp(request);
        return ResponseEntity.ok(ApiResponse.success("OTP resent successfully", response, httpRequest.getRequestURI()));
    }
}
