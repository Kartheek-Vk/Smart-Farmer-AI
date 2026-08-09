package com.smartfarmer.ai.exception;

/**
 * Raised when an external provider (AI service, weather provider) is not configured or not
 * reachable, so the API can answer 503 instead of returning invented data.
 */
public class ServiceUnavailableException extends SmartFarmerException {
    public ServiceUnavailableException(String message) {
        super(message);
    }
}
