package com.smartfarmer.ai.user.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record UpdateUserPreferenceRequest(
        @NotBlank @Size(min = 2, max = 10) String language,
        @NotBlank @Pattern(regexp = "METRIC|IMPERIAL", message = "measurementUnit must be METRIC or IMPERIAL")
        String measurementUnit,
        @NotNull Boolean emailNotificationsEnabled,
        @NotNull Boolean pushNotificationsEnabled,
        @NotNull Boolean weatherAlertsEnabled
) {
}
