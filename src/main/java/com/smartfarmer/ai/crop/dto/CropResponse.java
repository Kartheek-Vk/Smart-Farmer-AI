package com.smartfarmer.ai.crop.dto;

import java.util.UUID;

public record CropResponse(
        UUID id,
        String name,
        String category,
        String description,
        String season
) {}
