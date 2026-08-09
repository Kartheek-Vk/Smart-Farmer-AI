package com.smartfarmer.ai.weather.controller;

import com.smartfarmer.ai.common.api.ApiResponse;
import com.smartfarmer.ai.common.api.ApiResponseFactory;
import com.smartfarmer.ai.weather.dto.WeatherResponse;
import com.smartfarmer.ai.weather.service.WeatherService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Weather", description = "Weather data from the configured provider")
@RestController
@RequestMapping("/api/v1/weather")
public class WeatherController {

    private final WeatherService weatherService;
    private final ApiResponseFactory apiResponseFactory;

    public WeatherController(WeatherService weatherService, ApiResponseFactory apiResponseFactory) {
        this.weatherService = weatherService;
        this.apiResponseFactory = apiResponseFactory;
    }

    @GetMapping("/current")
    public ApiResponse<WeatherResponse> getCurrentWeather(@RequestParam String location,
                                                          @RequestParam BigDecimal latitude,
                                                          @RequestParam BigDecimal longitude,
                                                          HttpServletRequest request) {
        return apiResponseFactory.success("Current weather retrieved",
                weatherService.getCurrentWeather(location, latitude, longitude), request);
    }

    @GetMapping("/forecast")
    public ApiResponse<List<WeatherResponse>> getForecast(@RequestParam String location,
                                                          @RequestParam BigDecimal latitude,
                                                          @RequestParam BigDecimal longitude,
                                                          HttpServletRequest request) {
        return apiResponseFactory.success("Weather forecast retrieved",
                weatherService.getForecast(location, latitude, longitude), request);
    }

    @GetMapping("/alerts")
    public ApiResponse<List<WeatherResponse>> getAlerts(@RequestParam String location,
                                                        @RequestParam BigDecimal latitude,
                                                        @RequestParam BigDecimal longitude,
                                                        HttpServletRequest request) {
        return apiResponseFactory.success("Weather alerts retrieved",
                weatherService.getAlerts(location, latitude, longitude), request);
    }

    @GetMapping("/history")
    public ApiResponse<List<WeatherResponse>> getHistory(@RequestParam String location, HttpServletRequest request) {
        return apiResponseFactory.success("Weather history retrieved", weatherService.getHistory(location), request);
    }
}
