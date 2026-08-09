package com.smartfarmer.ai.integration.storage;

public record StoredFile(
        String storageKey,
        String originalFilename,
        String contentType,
        long size,
        String uri
) {
}
