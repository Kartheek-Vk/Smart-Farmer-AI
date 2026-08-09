package com.smartfarmer.ai.market.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public record MarketPriceTrendResponse(
        UUID marketId,
        UUID cropId,
        LocalDate from,
        LocalDate to,
        BigDecimal minPrice,
        BigDecimal maxPrice,
        BigDecimal averagePrice,
        List<MarketPricePoint> points
) {
    public record MarketPricePoint(LocalDate priceDate, BigDecimal price, String unit) {
    }
}
