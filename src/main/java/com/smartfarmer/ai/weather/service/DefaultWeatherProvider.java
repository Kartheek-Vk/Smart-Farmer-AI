package com.smartfarmer.ai.weather.service;

import com.smartfarmer.ai.weather.dto.WeatherResponse;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Collections;
import java.util.List;

@Service
public class DefaultWeatherProvider implements WeatherProvider {

    @Override
    public WeatherResponse getCurrentWeather(String location, BigDecimal lat, BigDecimal lng) {
        return new WeatherResponse(
                location, lat, lng, "CURRENT", Instant.now(), "{\"status\": \"pending_integration\", \"temperature\": 25.0}"
        );
    }

    @Override
    public List<WeatherResponse> getForecast(String location, BigDecimal lat, BigDecimal lng) {
        return Collections.singletonList(
                new WeatherResponse(location, lat, lng, "FORECAST", Instant.now(), "{\"status\": \"pending_integration\", \"forecast\": []}")
        );
    }

    @Override
    public List<WeatherResponse> getAlerts(String location, BigDecimal lat, BigDecimal lng) {
        return Collections.emptyList();
    }
}
