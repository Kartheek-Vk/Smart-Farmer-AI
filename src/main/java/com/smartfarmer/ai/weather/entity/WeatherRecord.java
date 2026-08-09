package com.smartfarmer.ai.weather.entity;

import com.smartfarmer.ai.common.model.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.Instant;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "weather_records")
public class WeatherRecord extends BaseEntity {

    @Column(nullable = false, length = 120)
    private String location;

    @Column(nullable = false, precision = 10, scale = 7)
    private BigDecimal latitude;

    @Column(nullable = false, precision = 10, scale = 7)
    private BigDecimal longitude;

    @Column(nullable = false, length = 40)
    private String recordType;

    @Column(nullable = false)
    private Instant observedAt;

    @Column(length = 2000)
    private String payloadJson;
}
