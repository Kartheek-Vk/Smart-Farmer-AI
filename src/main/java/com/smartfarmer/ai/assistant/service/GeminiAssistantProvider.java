package com.smartfarmer.ai.assistant.service;

import com.smartfarmer.ai.exception.ServiceUnavailableException;
import com.smartfarmer.ai.integration.ai.GeminiProperties;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

/**
 * Talks to Google's Generative Language API. The API key comes from configuration only and is
 * never returned to clients or logged.
 */
@Service
public class GeminiAssistantProvider implements AssistantProvider {

    private static final String BASE_URL = "https://generativelanguage.googleapis.com";

    private final GeminiProperties properties;
    private final RestClient restClient;

    public GeminiAssistantProvider(GeminiProperties properties, RestClient.Builder restClientBuilder) {
        this.properties = properties;
        this.restClient = restClientBuilder.baseUrl(BASE_URL).build();
    }

    @Override
    public boolean isAvailable() {
        return StringUtils.hasText(properties.apiKey());
    }

    @Override
    public String reply(List<AssistantTurn> history, String userMessage) {
        if (!isAvailable()) {
            throw new ServiceUnavailableException(
                    "No AI assistant provider is configured; set GEMINI_API_KEY to enable the assistant");
        }
        List<Map<String, Object>> contents = new ArrayList<>();
        for (AssistantTurn turn : history) {
            contents.add(Map.of(
                    "role", "ASSISTANT".equalsIgnoreCase(turn.role()) ? "model" : "user",
                    "parts", List.of(Map.of("text", turn.content()))));
        }
        contents.add(Map.of("role", "user", "parts", List.of(Map.of("text", userMessage))));

        try {
            GeminiResponse response = restClient.post()
                    .uri("/v1beta/models/{model}:generateContent", properties.model())
                    .contentType(MediaType.APPLICATION_JSON)
                    .header("x-goog-api-key", properties.apiKey())
                    .body(Map.of("contents", contents))
                    .retrieve()
                    .body(GeminiResponse.class);
            String text = extractText(response);
            if (!StringUtils.hasText(text)) {
                throw new ServiceUnavailableException("The AI assistant returned an empty response");
            }
            return text;
        } catch (RestClientException ex) {
            throw new ServiceUnavailableException("The AI assistant provider could not be reached");
        }
    }

    private String extractText(GeminiResponse response) {
        if (response == null || response.candidates() == null || response.candidates().isEmpty()) {
            return null;
        }
        GeminiResponse.Content content = response.candidates().get(0).content();
        if (content == null || content.parts() == null || content.parts().isEmpty()) {
            return null;
        }
        return content.parts().get(0).text();
    }

    record GeminiResponse(List<Candidate> candidates) {
        record Candidate(Content content) {
        }

        record Content(List<Part> parts) {
        }

        record Part(String text) {
        }
    }
}
