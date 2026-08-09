package com.smartfarmer.ai.assistant.service;

import java.util.List;

/**
 * Abstraction over the conversational AI provider. Implementations must never invent an answer:
 * when the provider is not configured {@link #isAvailable()} returns {@code false} and callers are
 * expected to surface that state to the client.
 */
public interface AssistantProvider {

    boolean isAvailable();

    String reply(List<AssistantTurn> history, String userMessage);

    record AssistantTurn(String role, String content) {
    }
}
