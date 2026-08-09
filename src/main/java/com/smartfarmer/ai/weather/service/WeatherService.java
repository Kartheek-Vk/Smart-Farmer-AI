package com.smartfarmer.ai.weather.service;

import com.smartfarmer.ai.weather.dto.WeatherResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class WeatherService {

    private final WeatherProvider weatherProvider;

    public WeatherResponse getCurrentWeather(String location, BigDecimal lat, BigDecimal lng) {
        return weatherProvider.getCurrentWeather(location, lat, lng);
    }

    public List<WeatherResponse> getForecast(String location, BigDecimal lat, BigDecimal lng) {
        return weatherProvider.getForecast(location, lat, lng);
    }

    public List<WeatherResponse> getAlerts(String location, BigDecimal lat, BigDecimal lng) {
        return weatherProvider.getAlerts(location, lat, lng);
    }
}
