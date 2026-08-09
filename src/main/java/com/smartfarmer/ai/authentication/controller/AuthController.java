package com.smartfarmer.ai.authentication.controller;

import com.smartfarmer.ai.authentication.dto.AuthResponse;
import com.smartfarmer.ai.authentication.dto.AuthUserResponse;
import com.smartfarmer.ai.authentication.dto.LoginRequest;
import com.smartfarmer.ai.authentication.dto.OtpRequest;
import com.smartfarmer.ai.authentication.dto.OtpResponse;
import com.smartfarmer.ai.authentication.dto.OtpVerificationRequest;
import com.smartfarmer.ai.authentication.dto.RefreshTokenRequest;
import com.smartfarmer.ai.authentication.dto.RegisterRequest;
import com.smartfarmer.ai.authentication.dto.ResetPasswordRequest;
import com.smartfarmer.ai.authentication.service.AuthService;
import com.smartfarmer.ai.common.api.ApiResponse;
import com.smartfarmer.ai.common.api.ApiResponseFactory;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Authentication", description = "Registration, login, token lifecycle and OTP flows")
@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthService authService;
    private final ApiResponseFactory apiResponseFactory;

    public AuthController(AuthService authService, ApiResponseFactory apiResponseFactory) {
        this.authService = authService;
        this.apiResponseFactory = apiResponseFactory;
    }

    @Operation(summary = "Register a new account")
    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<AuthResponse> register(@Valid @RequestBody RegisterRequest request, HttpServletRequest httpRequest) {
        return apiResponseFactory.success("User registered successfully", authService.register(request), httpRequest);
    }

    @Operation(summary = "Authenticate and obtain an access/refresh token pair")
    @PostMapping("/login")
    public ApiResponse<AuthResponse> login(@Valid @RequestBody LoginRequest request, HttpServletRequest httpRequest) {
        return apiResponseFactory.success("Logged in successfully", authService.login(request), httpRequest);
    }

    @Operation(summary = "Rotate a refresh token for a new token pair")
    @PostMapping("/refresh")
    public ApiResponse<AuthResponse> refresh(@Valid @RequestBody RefreshTokenRequest request, HttpServletRequest httpRequest) {
        return apiResponseFactory.success("Token refreshed successfully", authService.refresh(request), httpRequest);
    }

    @Operation(summary = "Revoke a refresh token")
    @PostMapping("/logout")
    public ApiResponse<Void> logout(@Valid @RequestBody RefreshTokenRequest request, HttpServletRequest httpRequest) {
        authService.logout(request);
        return apiResponseFactory.success("Logged out successfully", null, httpRequest);
    }

    @Operation(summary = "Get the authenticated user")
    @GetMapping("/me")
    public ApiResponse<AuthUserResponse> getCurrentUser(HttpServletRequest httpRequest) {
        return apiResponseFactory.success("Current user retrieved successfully", authService.getCurrentUser(), httpRequest);
    }

    @Operation(summary = "Start the password reset flow")
    @PostMapping("/forgot-password")
    public ApiResponse<OtpResponse> forgotPassword(@Valid @RequestBody OtpRequest request, HttpServletRequest httpRequest) {
        return apiResponseFactory.success("Password reset OTP requested", authService.forgotPassword(request), httpRequest);
    }

    @Operation(summary = "Complete the password reset flow with an OTP")
    @PostMapping("/reset-password")
    public ApiResponse<Void> resetPassword(@Valid @RequestBody ResetPasswordRequest request, HttpServletRequest httpRequest) {
        authService.resetPassword(request);
        return apiResponseFactory.success("Password reset successfully", null, httpRequest);
    }

    @Operation(summary = "Verify an OTP code")
    @PostMapping("/verify-otp")
    public ApiResponse<OtpResponse> verifyOtp(@Valid @RequestBody OtpVerificationRequest request, HttpServletRequest httpRequest) {
        return apiResponseFactory.success("OTP verified successfully", authService.verifyOtp(request), httpRequest);
    }

    @Operation(summary = "Resend an OTP code")
    @PostMapping("/resend-otp")
    public ApiResponse<OtpResponse> resendOtp(@Valid @RequestBody OtpRequest request, HttpServletRequest httpRequest) {
        return apiResponseFactory.success("OTP resent", authService.resendOtp(request), httpRequest);
    }
}
