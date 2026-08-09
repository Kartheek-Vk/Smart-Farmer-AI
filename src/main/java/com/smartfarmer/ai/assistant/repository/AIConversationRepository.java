package com.smartfarmer.ai.assistant.repository;

import com.smartfarmer.ai.assistant.entity.AIConversation;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AIConversationRepository extends JpaRepository<AIConversation, UUID> {

    List<AIConversation> findByUserIdOrderByUpdatedAtDesc(UUID userId);
    
    org.springframework.data.domain.Page<AIConversation> findByUserId(UUID userId, org.springframework.data.domain.Pageable pageable);
}
