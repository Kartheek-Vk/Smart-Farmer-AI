package com.smartfarmer.ai.farm.dto;

import com.smartfarmer.ai.common.enums.AreaUnit;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;

public record UpdateFieldRequest(
        @NotBlank String name,
        @NotNull @Positive BigDecimal area,
        @NotNull AreaUnit areaUnit,
        String notes
) {}
