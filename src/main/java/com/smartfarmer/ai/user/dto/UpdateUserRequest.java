package com.smartfarmer.ai.user.dto;

import jakarta.validation.constraints.Size;

public record UpdateUserRequest(
        @Size(min = 2, max = 50) String firstName,
        @Size(min = 2, max = 50) String lastName
) {
}
