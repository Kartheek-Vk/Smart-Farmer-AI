package com.smartfarmer.ai.integration.ai;

import com.smartfarmer.ai.disease.dto.DiseaseAnalysisResponse;
import com.smartfarmer.ai.recommendation.dto.RecommendationDecision;

public interface AiServiceClient {
    DiseaseAnalysisResponse analyzeDisease(byte[] fileContent, String contentType, String filename);

    RecommendationDecision generateCropRecommendation(String prompt);

    RecommendationDecision generateFertilizerRecommendation(String prompt);

    RecommendationDecision generateIrrigationRecommendation(String prompt);
}
