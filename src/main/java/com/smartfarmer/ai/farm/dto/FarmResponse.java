package com.smartfarmer.ai.farm.dto;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record FarmResponse(
        UUID id,
        String name,
        String location,
        BigDecimal latitude,
        BigDecimal longitude,
        BigDecimal area,
        String areaUnit,
        String soilType,
        String irrigationType,
        String ownershipType,
        Instant createdAt,
        Instant updatedAt
) {}
