package com.smartfarmer.ai.recommendation.dto;

import com.smartfarmer.ai.common.enums.RecommendationStatus;

public record RecommendationDecision(
        RecommendationStatus status,
        String summary
) {
}
