package com.smartfarmer.ai.authentication.dto;

import com.smartfarmer.ai.common.enums.OtpPurpose;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record OtpRequest(
        @NotBlank @Email String email,
        @NotNull OtpPurpose purpose
) {
}
