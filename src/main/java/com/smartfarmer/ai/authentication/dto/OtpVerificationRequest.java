package com.smartfarmer.ai.authentication.dto;

import com.smartfarmer.ai.common.enums.TokenType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record OtpVerificationRequest(
        @Email String email,
        @NotBlank String code,
        @NotNull TokenType type
) {
}
