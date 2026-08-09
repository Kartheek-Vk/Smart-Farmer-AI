package com.smartfarmer.ai.crop.dto;

import com.smartfarmer.ai.common.enums.CropSeasonStatus;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;

public record UpdateCropSeasonRequest(
        @NotNull LocalDate startDate,
        LocalDate endDate,
        @NotNull CropSeasonStatus status,
        @Size(max = 255) String notes
) {
}
