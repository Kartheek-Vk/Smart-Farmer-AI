package com.smartfarmer.ai.assistant.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record SendMessageRequest(
        @NotBlank(message = "Content is required")
        @Size(max = 4000, message = "Content must be less than 4000 characters")
        String content
) {
}
