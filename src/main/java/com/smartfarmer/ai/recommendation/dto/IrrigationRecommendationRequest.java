package com.smartfarmer.ai.recommendation.dto;

import jakarta.validation.constraints.NotBlank;
import java.util.UUID;

public record IrrigationRecommendationRequest(
        UUID farmId,
        @NotBlank String cropName,
        @NotBlank String soilType,
        String area,
        String climate,
        String waterSource,
        String additionalInfo
) {
}
