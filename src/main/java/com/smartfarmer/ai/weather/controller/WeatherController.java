package com.smartfarmer.ai.weather.controller;

import com.smartfarmer.ai.common.api.ApiResponse;
import com.smartfarmer.ai.common.api.ApiResponseFactory;
import com.smartfarmer.ai.weather.dto.WeatherResponse;
import com.smartfarmer.ai.weather.service.WeatherService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/v1/weather")
@RequiredArgsConstructor
public class WeatherController {

    private final WeatherService weatherService;
    private final ApiResponseFactory apiResponseFactory;

    @GetMapping("/current")
    public ResponseEntity<ApiResponse<WeatherResponse>> getCurrentWeather(
            @RequestParam String location,
            @RequestParam BigDecimal latitude,
            @RequestParam BigDecimal longitude) {
        WeatherResponse response = weatherService.getCurrentWeather(location, latitude, longitude);
        return ResponseEntity.ok(apiResponseFactory.success("Current weather retrieved", response));
    }

    @GetMapping("/forecast")
    public ResponseEntity<ApiResponse<List<WeatherResponse>>> getForecast(
            @RequestParam String location,
            @RequestParam BigDecimal latitude,
            @RequestParam BigDecimal longitude) {
        List<WeatherResponse> response = weatherService.getForecast(location, latitude, longitude);
        return ResponseEntity.ok(apiResponseFactory.success("Weather forecast retrieved", response));
    }

    @GetMapping("/alerts")
    public ResponseEntity<ApiResponse<List<WeatherResponse>>> getAlerts(
            @RequestParam String location,
            @RequestParam BigDecimal latitude,
            @RequestParam BigDecimal longitude) {
        List<WeatherResponse> response = weatherService.getAlerts(location, latitude, longitude);
        return ResponseEntity.ok(apiResponseFactory.success("Weather alerts retrieved", response));
    }
}
