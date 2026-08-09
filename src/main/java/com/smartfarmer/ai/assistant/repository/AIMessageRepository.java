package com.smartfarmer.ai.assistant.repository;

import com.smartfarmer.ai.assistant.entity.AIMessage;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AIMessageRepository extends JpaRepository<AIMessage, UUID> {

    List<AIMessage> findByConversationIdOrderByCreatedAtAsc(UUID conversationId);
}
