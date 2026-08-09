package com.smartfarmer.ai.integration.ai;

import com.smartfarmer.ai.disease.dto.DiseaseAnalysisResponse;
import com.smartfarmer.ai.recommendation.dto.RecommendationDecision;

/**
 * Boundary towards the external AI service. No model inference happens inside this backend: when
 * no provider is configured {@link #isAvailable()} is {@code false} and callers must record the
 * request as pending instead of inventing a result.
 */
public interface AiServiceClient {

    boolean isAvailable();

    DiseaseAnalysisResponse analyzeDisease(byte[] fileContent, String contentType, String filename);

    RecommendationDecision generateCropRecommendation(String prompt);

    RecommendationDecision generateFertilizerRecommendation(String prompt);

    RecommendationDecision generateIrrigationRecommendation(String prompt);
}
