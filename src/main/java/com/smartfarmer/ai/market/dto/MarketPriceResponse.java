package com.smartfarmer.ai.market.dto;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

public record MarketPriceResponse(
        UUID id,
        UUID marketId,
        String marketName,
        UUID cropId,
        String cropName,
        BigDecimal price,
        String unit,
        LocalDate priceDate,
        Instant createdAt
) {
}
