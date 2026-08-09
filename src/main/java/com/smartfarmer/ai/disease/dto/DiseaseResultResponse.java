package com.smartfarmer.ai.disease.dto;

import java.util.UUID;

public record DiseaseResultResponse(
        UUID id,
        String diseaseName,
        double confidence,
        String summary,
        String recommendation
) {
}
