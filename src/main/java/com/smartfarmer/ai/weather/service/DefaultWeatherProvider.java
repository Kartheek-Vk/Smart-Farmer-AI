package com.smartfarmer.ai.weather.service;

import com.smartfarmer.ai.exception.ServiceUnavailableException;
import com.smartfarmer.ai.weather.WeatherProperties;
import com.smartfarmer.ai.weather.dto.WeatherResponse;
import java.math.BigDecimal;
import java.time.Duration;
import java.time.Instant;
import java.util.List;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

/**
 * Reads weather from the provider configured through {@code app.weather.base-url}. Without that
 * configuration every call fails with 503 rather than returning fabricated measurements.
 */
@Service
public class DefaultWeatherProvider implements WeatherProvider {

    private final WeatherProperties properties;
    private final RestClient restClient;

    public DefaultWeatherProvider(WeatherProperties properties, RestClient.Builder restClientBuilder) {
        this.properties = properties;
        this.restClient = StringUtils.hasText(properties.baseUrl())
                ? restClientBuilder.baseUrl(properties.baseUrl()).requestFactory(requestFactory(properties.timeout())).build()
                : null;
    }

    @Override
    public boolean isAvailable() {
        return restClient != null;
    }

    @Override
    public WeatherResponse getCurrentWeather(String location, BigDecimal lat, BigDecimal lng) {
        String payload = fetch("/current", lat, lng);
        return new WeatherResponse(location, lat, lng, "CURRENT", Instant.now(), payload);
    }

    @Override
    public List<WeatherResponse> getForecast(String location, BigDecimal lat, BigDecimal lng) {
        String payload = fetch("/forecast", lat, lng);
        return List.of(new WeatherResponse(location, lat, lng, "FORECAST", Instant.now(), payload));
    }

    @Override
    public List<WeatherResponse> getAlerts(String location, BigDecimal lat, BigDecimal lng) {
        String payload = fetch("/alerts", lat, lng);
        return List.of(new WeatherResponse(location, lat, lng, "ALERT", Instant.now(), payload));
    }

    private String fetch(String path, BigDecimal lat, BigDecimal lng) {
        requireAvailable();
        try {
            return restClient.get()
                    .uri(uriBuilder -> uriBuilder.path(path)
                            .queryParam("lat", lat)
                            .queryParam("lon", lng)
                            .queryParamIfPresent("apiKey", java.util.Optional.ofNullable(
                                    StringUtils.hasText(properties.apiKey()) ? properties.apiKey() : null))
                            .build())
                    .retrieve()
                    .body(String.class);
        } catch (RestClientException ex) {
            throw new ServiceUnavailableException("The weather provider could not be reached");
        }
    }

    private void requireAvailable() {
        if (!isAvailable()) {
            throw new ServiceUnavailableException(
                    "No weather provider is configured; set app.weather.base-url to enable weather data");
        }
    }

    private static SimpleClientHttpRequestFactory requestFactory(int timeoutMillis) {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        int effective = timeoutMillis > 0 ? timeoutMillis : 5000;
        factory.setConnectTimeout(Duration.ofMillis(effective));
        factory.setReadTimeout(Duration.ofMillis(effective));
        return factory;
    }
}
