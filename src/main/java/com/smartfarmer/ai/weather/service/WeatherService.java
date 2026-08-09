package com.smartfarmer.ai.weather.service;

import com.smartfarmer.ai.weather.dto.WeatherResponse;
import com.smartfarmer.ai.weather.entity.WeatherRecord;
import com.smartfarmer.ai.weather.repository.WeatherRecordRepository;
import java.math.BigDecimal;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class WeatherService {

    private final WeatherProvider weatherProvider;
    private final WeatherRecordRepository weatherRecordRepository;

    public WeatherService(WeatherProvider weatherProvider, WeatherRecordRepository weatherRecordRepository) {
        this.weatherProvider = weatherProvider;
        this.weatherRecordRepository = weatherRecordRepository;
    }

    @Transactional
    public WeatherResponse getCurrentWeather(String location, BigDecimal lat, BigDecimal lng) {
        return persist(weatherProvider.getCurrentWeather(location, lat, lng));
    }

    @Transactional
    public List<WeatherResponse> getForecast(String location, BigDecimal lat, BigDecimal lng) {
        return weatherProvider.getForecast(location, lat, lng).stream().map(this::persist).toList();
    }

    @Transactional
    public List<WeatherResponse> getAlerts(String location, BigDecimal lat, BigDecimal lng) {
        return weatherProvider.getAlerts(location, lat, lng).stream().map(this::persist).toList();
    }

    @Transactional(readOnly = true)
    public List<WeatherResponse> getHistory(String location) {
        return weatherRecordRepository.findByLocationOrderByObservedAtDesc(location).stream()
                .map(record -> new WeatherResponse(record.getLocation(), record.getLatitude(), record.getLongitude(),
                        record.getRecordType(), record.getObservedAt(), record.getPayloadJson()))
                .toList();
    }

    private WeatherResponse persist(WeatherResponse response) {
        WeatherRecord record = new WeatherRecord();
        record.setLocation(response.location());
        record.setLatitude(response.latitude());
        record.setLongitude(response.longitude());
        record.setRecordType(response.recordType());
        record.setObservedAt(response.observedAt());
        record.setPayloadJson(truncate(response.payloadJson()));
        weatherRecordRepository.save(record);
        return response;
    }

    private String truncate(String payload) {
        if (payload == null || payload.length() <= 2000) {
            return payload;
        }
        return payload.substring(0, 2000);
    }
}
