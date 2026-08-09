package com.smartfarmer.ai.security;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app.jwt")
public record JwtProperties(
        String secret,
        long accessExpiration,
        long refreshExpiration
) {
}
