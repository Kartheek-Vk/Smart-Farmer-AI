package com.smartfarmer.ai.weather.dto;

import java.math.BigDecimal;
import java.time.Instant;

public record WeatherResponse(
        String location,
        BigDecimal latitude,
        BigDecimal longitude,
        String recordType,
        Instant observedAt,
        String payloadJson
) {
}
