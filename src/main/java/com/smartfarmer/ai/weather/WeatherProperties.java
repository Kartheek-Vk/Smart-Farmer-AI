package com.smartfarmer.ai.weather;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.DefaultValue;

@ConfigurationProperties(prefix = "app.weather")
public record WeatherProperties(
        String baseUrl,
        String apiKey,
        @DefaultValue("5000") int timeout
) {
}
