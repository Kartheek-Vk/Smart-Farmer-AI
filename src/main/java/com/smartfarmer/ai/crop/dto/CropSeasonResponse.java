package com.smartfarmer.ai.crop.dto;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

public record CropSeasonResponse(
        UUID id,
        UUID farmId,
        UUID fieldId,
        UUID cropId,
        String cropName,
        LocalDate startDate,
        LocalDate endDate,
        String status,
        String notes,
        Instant createdAt,
        Instant updatedAt
) {}
