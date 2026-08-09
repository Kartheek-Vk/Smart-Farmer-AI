package com.smartfarmer.ai.crop.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.UUID;

public record CreateCropSeasonRequest(
        @NotNull UUID farmId,
        UUID fieldId,
        @NotNull UUID cropId,
        @NotNull LocalDate startDate,
        LocalDate endDate,
        @NotBlank String status,
        String notes
) {}
