package com.smartfarmer.ai.user.dto;

public record UserPreferenceResponse(
        String language,
        String measurementUnit,
        boolean emailNotificationsEnabled,
        boolean pushNotificationsEnabled,
        boolean weatherAlertsEnabled
) {
}
