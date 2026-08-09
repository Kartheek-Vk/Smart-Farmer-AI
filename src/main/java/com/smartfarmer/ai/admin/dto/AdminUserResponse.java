package com.smartfarmer.ai.admin.dto;

import java.time.Instant;
import java.util.Set;
import java.util.UUID;

public record AdminUserResponse(
        UUID id,
        String email,
        String firstName,
        String lastName,
        String status,
        Set<String> roles,
        Instant createdAt,
        Instant updatedAt
) {
}
