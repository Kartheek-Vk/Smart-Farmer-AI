package com.smartfarmer.ai.disease.dto;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record DiseaseScanResponse(
        UUID id,
        UUID userId,
        UUID farmId,
        String imageUri,
        String originalFilename,
        String contentType,
        long fileSize,
        String status,
        List<DiseaseResultResponse> results,
        Instant createdAt
) {
}
