package com.smartfarmer.ai.recommendation.dto;

import jakarta.validation.constraints.NotBlank;
import java.util.UUID;

public record CropRecommendationRequest(
        UUID farmId,
        @NotBlank String soilType,
        @NotBlank String season,
        String climate,
        String irrigationType,
        String additionalInfo
) {
}
