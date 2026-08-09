package com.smartfarmer.ai.integration.ai;

import com.smartfarmer.ai.common.enums.RecommendationStatus;
import com.smartfarmer.ai.disease.dto.DiseaseAnalysisResponse;
import com.smartfarmer.ai.recommendation.dto.RecommendationDecision;
import java.time.Duration;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.MediaType;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

/**
 * Calls the external AI service over HTTP when {@code app.ai-service.base-url} is configured.
 * Without that configuration the client reports itself unavailable so that no synthetic
 * prediction is ever persisted.
 */
@Service
public class DefaultAiServiceClient implements AiServiceClient {

    private final AiServiceProperties properties;
    private final RestClient restClient;

    public DefaultAiServiceClient(AiServiceProperties properties, RestClient.Builder restClientBuilder) {
        this.properties = properties;
        this.restClient = StringUtils.hasText(properties.baseUrl())
                ? restClientBuilder
                    .baseUrl(properties.baseUrl())
                    .requestFactory(requestFactory(properties.timeout()))
                    .build()
                : null;
    }

    @Override
    public boolean isAvailable() {
        return restClient != null;
    }

    @Override
    public DiseaseAnalysisResponse analyzeDisease(byte[] fileContent, String contentType, String filename) {
        requireAvailable();
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("file", new ByteArrayResource(fileContent) {
            @Override
            public String getFilename() {
                return filename;
            }
        });
        try {
            return restClient.post()
                    .uri("/api/v1/disease/analyze")
                    .contentType(MediaType.MULTIPART_FORM_DATA)
                    .headers(headers -> applyApiKey(headers::set))
                    .body(body)
                    .retrieve()
                    .body(DiseaseAnalysisResponse.class);
        } catch (RestClientException ex) {
            throw new AiServiceException("Disease analysis request to the AI service failed");
        }
    }

    @Override
    public RecommendationDecision generateCropRecommendation(String prompt) {
        return generate("/api/v1/recommendations/crop", prompt);
    }

    @Override
    public RecommendationDecision generateFertilizerRecommendation(String prompt) {
        return generate("/api/v1/recommendations/fertilizer", prompt);
    }

    @Override
    public RecommendationDecision generateIrrigationRecommendation(String prompt) {
        return generate("/api/v1/recommendations/irrigation", prompt);
    }

    private RecommendationDecision generate(String path, String prompt) {
        requireAvailable();
        try {
            RecommendationDecision decision = restClient.post()
                    .uri(path)
                    .contentType(MediaType.APPLICATION_JSON)
                    .headers(headers -> applyApiKey(headers::set))
                    .body(new PromptPayload(prompt))
                    .retrieve()
                    .body(RecommendationDecision.class);
            if (decision == null || decision.status() == null) {
                throw new AiServiceException("AI service returned an empty recommendation");
            }
            return decision;
        } catch (RestClientException ex) {
            return new RecommendationDecision(RecommendationStatus.FAILED, null);
        }
    }

    private void applyApiKey(HeaderSetter setter) {
        if (StringUtils.hasText(properties.apiKey())) {
            setter.set("X-API-Key", properties.apiKey());
        }
    }

    private void requireAvailable() {
        if (!isAvailable()) {
            throw new AiServiceException("AI service is not configured");
        }
    }

    private static SimpleClientHttpRequestFactory requestFactory(int timeoutMillis) {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        int effective = timeoutMillis > 0 ? timeoutMillis : 5000;
        factory.setConnectTimeout(Duration.ofMillis(effective));
        factory.setReadTimeout(Duration.ofMillis(effective));
        return factory;
    }

    private record PromptPayload(String prompt) {
    }

    @FunctionalInterface
    private interface HeaderSetter {
        void set(String name, String value);
    }
}
