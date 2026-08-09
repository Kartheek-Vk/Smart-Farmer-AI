package com.smartfarmer.ai.integration.ai;

import com.smartfarmer.ai.common.enums.RecommendationStatus;
import com.smartfarmer.ai.disease.dto.DiseaseAnalysisResponse;
import com.smartfarmer.ai.recommendation.dto.RecommendationDecision;
import org.springframework.stereotype.Service;

@Service
public class DefaultAiServiceClient implements AiServiceClient {

    @Override
    public DiseaseAnalysisResponse analyzeDisease(byte[] fileContent, String contentType, String filename) {
        return new DiseaseAnalysisResponse(
                "PENDING_EXTERNAL_ANALYSIS",
                "Disease analysis is queued for the external AI service boundary",
                0.0,
                "FastAPI AI service integration is prepared but not connected in this environment"
        );
    }

    @Override
    public RecommendationDecision generateCropRecommendation(String prompt) {
        return new RecommendationDecision(RecommendationStatus.GENERATED, "Crop recommendation request recorded and ready for AI provider integration");
    }

    @Override
    public RecommendationDecision generateFertilizerRecommendation(String prompt) {
        return new RecommendationDecision(RecommendationStatus.GENERATED, "Fertilizer recommendation request recorded and ready for AI provider integration");
    }

    @Override
    public RecommendationDecision generateIrrigationRecommendation(String prompt) {
        return new RecommendationDecision(RecommendationStatus.GENERATED, "Irrigation recommendation request recorded and ready for AI provider integration");
    }
}
