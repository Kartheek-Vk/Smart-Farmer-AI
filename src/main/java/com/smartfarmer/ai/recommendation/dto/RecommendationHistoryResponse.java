package com.smartfarmer.ai.recommendation.dto;

import java.time.Instant;
import java.util.UUID;

public record RecommendationHistoryResponse(
        UUID id,
        String recommendationType,
        String referenceId,
        String summary,
        Instant createdAt
) {
}
