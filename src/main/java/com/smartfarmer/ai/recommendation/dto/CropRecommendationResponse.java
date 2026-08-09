package com.smartfarmer.ai.recommendation.dto;

import java.time.Instant;
import java.util.UUID;

public record CropRecommendationResponse(
        UUID id,
        UUID farmId,
        String inputSummary,
        String recommendationText,
        String status,
        Instant createdAt
) {
}
