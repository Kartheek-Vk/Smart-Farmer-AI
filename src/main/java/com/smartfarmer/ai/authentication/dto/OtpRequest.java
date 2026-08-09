package com.smartfarmer.ai.authentication.dto;

import com.smartfarmer.ai.common.enums.TokenType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

public record OtpRequest(
        @Email String email,
        @NotNull TokenType type
) {
}
