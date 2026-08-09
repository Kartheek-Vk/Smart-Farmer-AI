package com.smartfarmer.ai.admin.dto;

import java.time.Instant;
import java.util.UUID;

public record AuditLogResponse(
        UUID id,
        UUID actorUserId,
        String action,
        String targetType,
        String targetId,
        String details,
        String ipAddress,
        Instant createdAt
) {
}
