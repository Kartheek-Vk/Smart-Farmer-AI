package com.smartfarmer.ai.integration.storage;

import java.util.List;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app.storage")
public record StorageProperties(
        String localDirectory,
        long maxFileSizeBytes,
        List<String> allowedContentTypes
) {
}
