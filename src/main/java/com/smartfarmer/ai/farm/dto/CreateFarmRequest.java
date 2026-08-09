package com.smartfarmer.ai.farm.dto;

import com.smartfarmer.ai.common.enums.AreaUnit;
import com.smartfarmer.ai.common.enums.IrrigationType;
import com.smartfarmer.ai.common.enums.OwnershipType;
import com.smartfarmer.ai.common.enums.SoilType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;

public record CreateFarmRequest(
        @NotBlank String name,
        @NotBlank String location,
        BigDecimal latitude,
        BigDecimal longitude,
        @NotNull @Positive BigDecimal area,
        @NotNull AreaUnit areaUnit,
        @NotNull SoilType soilType,
        @NotNull IrrigationType irrigationType,
        @NotNull OwnershipType ownershipType
) {}
