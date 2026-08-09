package com.smartfarmer.ai.authentication;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.DefaultValue;

@ConfigurationProperties(prefix = "app.otp")
public record OtpProperties(
        @DefaultValue("600") long ttlSeconds,
        @DefaultValue("false") boolean exposeDevCodeInResponse
) {
}
