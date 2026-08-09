package com.smartfarmer.ai.notification.repository;

import com.smartfarmer.ai.notification.entity.Notification;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository extends JpaRepository<Notification, UUID> {

    List<Notification> findByUserIdOrderByCreatedAtDesc(UUID userId);

    org.springframework.data.domain.Page<Notification> findByUserId(UUID userId, org.springframework.data.domain.Pageable pageable);
}
