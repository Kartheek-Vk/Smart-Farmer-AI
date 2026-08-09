package com.smartfarmer.ai.authentication.dto;

import com.smartfarmer.ai.common.enums.OtpPurpose;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record OtpVerificationRequest(
        @NotBlank @Email String email,
        @NotBlank @Size(min = 6, max = 12) String code,
        @NotNull OtpPurpose purpose
) {
}
