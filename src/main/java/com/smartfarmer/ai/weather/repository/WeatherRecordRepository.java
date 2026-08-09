package com.smartfarmer.ai.weather.repository;

import com.smartfarmer.ai.weather.entity.WeatherRecord;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WeatherRecordRepository extends JpaRepository<WeatherRecord, UUID> {

    List<WeatherRecord> findByLocationOrderByObservedAtDesc(String location);
}
