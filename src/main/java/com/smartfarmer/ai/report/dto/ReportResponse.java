package com.smartfarmer.ai.report.dto;

import java.time.Instant;
import java.util.UUID;

public record ReportResponse(
        UUID id,
        UUID farmId,
        String reportType,
        String title,
        String status,
        String metadataJson,
        Instant createdAt
) {
}
