package com.smartfarmer.ai.scheme.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record SchemeRequest(
        @NotBlank @Size(max = 160) String title,
        @NotBlank @Size(max = 80) String category,
        @NotBlank @Size(max = 120) String state,
        @NotBlank @Size(max = 1000) String eligibility,
        @NotNull Boolean active,
        @Size(max = 2000) String description
) {
}
