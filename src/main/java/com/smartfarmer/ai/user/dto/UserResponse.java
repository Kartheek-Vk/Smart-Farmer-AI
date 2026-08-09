package com.smartfarmer.ai.user.dto;

import java.time.Instant;
import java.util.Set;
import java.util.UUID;

public record UserResponse(
        UUID id,
        String email,
        String firstName,
        String lastName,
        String status,
        Set<String> roles,
        Instant createdAt
) {}
