package com.smartfarmer.ai.market.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateMarketRequest(
        @NotBlank @Size(max = 150) String name,
        @NotBlank @Size(max = 150) String location,
        @NotBlank @Size(max = 120) String state
) {
}
