package com.smartfarmer.ai.assistant.dto;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record ConversationDetailResponse(
        UUID id,
        String title,
        List<MessageResponse> messages,
        Instant createdAt,
        Instant updatedAt
) {
}
