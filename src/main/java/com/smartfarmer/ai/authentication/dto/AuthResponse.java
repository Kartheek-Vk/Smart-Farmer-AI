package com.smartfarmer.ai.authentication.dto;

public record AuthResponse(
        String accessToken,
        String refreshToken,
        String tokenType,
        long expiresInSeconds,
        AuthUserResponse user
) {
}
