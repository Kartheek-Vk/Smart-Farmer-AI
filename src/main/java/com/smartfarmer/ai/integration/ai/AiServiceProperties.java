package com.smartfarmer.ai.integration.ai;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app.ai-service")
public record AiServiceProperties(
        String baseUrl,
        String apiKey,
        int timeout
) {
}
