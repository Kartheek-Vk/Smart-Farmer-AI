package com.smartfarmer.ai.farmer.dto;

import jakarta.validation.constraints.Size;

public record UpdateFarmerProfileRequest(
        @Size(max = 120) String experienceLevel,
        @Size(max = 120) String primaryCrop,
        @Size(max = 255) String address
) {
}
