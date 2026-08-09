package com.smartfarmer.ai.recommendation.dto;

import jakarta.validation.constraints.NotBlank;
import java.util.UUID;

public record FertilizerRecommendationRequest(
        UUID farmId,
        @NotBlank String cropName,
        @NotBlank String soilType,
        String soilPh,
        String nitrogen,
        String phosphorus,
        String potassium,
        String additionalInfo
) {
}
