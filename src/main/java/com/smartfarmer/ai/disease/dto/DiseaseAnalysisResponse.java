package com.smartfarmer.ai.disease.dto;

public record DiseaseAnalysisResponse(
        String diseaseName,
        String summary,
        double confidence,
        String recommendation
) {
}
