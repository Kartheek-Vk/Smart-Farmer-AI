package com.smartfarmer.ai.weather.service;

import com.smartfarmer.ai.weather.dto.WeatherResponse;
import java.math.BigDecimal;
import java.util.List;

public interface WeatherProvider {

    boolean isAvailable();

    WeatherResponse getCurrentWeather(String location, BigDecimal lat, BigDecimal lng);

    List<WeatherResponse> getForecast(String location, BigDecimal lat, BigDecimal lng);

    List<WeatherResponse> getAlerts(String location, BigDecimal lat, BigDecimal lng);
}
