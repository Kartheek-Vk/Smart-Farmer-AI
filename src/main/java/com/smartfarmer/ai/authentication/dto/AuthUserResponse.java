package com.smartfarmer.ai.authentication.dto;

import com.smartfarmer.ai.common.enums.UserRole;
import com.smartfarmer.ai.common.enums.UserStatus;
import java.util.Set;
import java.util.UUID;

public record AuthUserResponse(
        UUID id,
        String email,
        String firstName,
        String lastName,
        UserStatus status,
        Set<UserRole> roles
) {
}
