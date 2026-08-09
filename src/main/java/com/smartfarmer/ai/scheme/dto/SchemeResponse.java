package com.smartfarmer.ai.scheme.dto;

import java.time.Instant;
import java.util.UUID;

public record SchemeResponse(
        UUID id,
        String title,
        String category,
        String state,
        String eligibility,
        boolean active,
        String description,
        Instant createdAt
) {
}
