package com.smartfarmer.ai.admin.dto;

import com.smartfarmer.ai.common.enums.NotificationType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.UUID;

public record BroadcastNotificationRequest(
        UUID userId,
        @NotNull NotificationType type,
        @NotBlank @Size(max = 200) String title,
        @NotBlank @Size(max = 1000) String message
) {
}
