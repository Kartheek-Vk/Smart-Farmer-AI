package com.smartfarmer.ai.authentication.dto;

/**
 * The {@code code} field is only populated for local development profiles where no mail or SMS
 * provider is configured; it is {@code null} in every other profile.
 */
public record OtpResponse(
        String message,
        String code
) {
}
