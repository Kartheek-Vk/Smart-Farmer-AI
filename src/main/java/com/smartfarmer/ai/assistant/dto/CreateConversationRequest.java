package com.smartfarmer.ai.assistant.dto;

import jakarta.validation.constraints.NotBlank;

public record CreateConversationRequest(
        @NotBlank(message = "Title is required")
        String title
) {
}
