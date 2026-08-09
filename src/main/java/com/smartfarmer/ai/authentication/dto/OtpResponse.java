package com.smartfarmer.ai.authentication.dto;

public record OtpResponse(
        String message,
        String code
) {
}
