package com.smartfarmer.ai.market.dto;

import java.time.Instant;
import java.util.UUID;

public record MarketResponse(
        UUID id,
        String name,
        String location,
        String state,
        Instant createdAt
) {
}
