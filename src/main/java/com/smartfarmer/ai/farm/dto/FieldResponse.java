package com.smartfarmer.ai.farm.dto;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record FieldResponse(
        UUID id,
        UUID farmId,
        String name,
        BigDecimal area,
        String areaUnit,
        String notes,
        Instant createdAt,
        Instant updatedAt
) {}
