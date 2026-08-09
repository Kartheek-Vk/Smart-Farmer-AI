package com.smartfarmer.ai.farmer.dto;

import java.util.UUID;

public record FarmerProfileResponse(
        UUID id,
        UUID userId,
        String experienceLevel,
        String primaryCrop,
        String address
) {
}
