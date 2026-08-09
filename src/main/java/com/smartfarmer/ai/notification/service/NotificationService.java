package com.smartfarmer.ai.notification.service;

import com.smartfarmer.ai.common.enums.NotificationType;
import com.smartfarmer.ai.exception.ResourceNotFoundException;
import com.smartfarmer.ai.exception.UnauthorizedException;
import com.smartfarmer.ai.notification.dto.NotificationResponse;
import com.smartfarmer.ai.notification.entity.Notification;
import com.smartfarmer.ai.notification.repository.NotificationRepository;
import com.smartfarmer.ai.user.entity.User;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class NotificationService {

    private final NotificationRepository notificationRepository;

    public NotificationService(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    @Transactional(readOnly = true)
    public Page<NotificationResponse> getNotifications(User user, Pageable pageable) {
        return notificationRepository.findByUserId(user.getId(), pageable)
                .map(this::mapToResponse);
    }

    @Transactional(readOnly = true)
    public NotificationResponse getNotificationById(UUID id, User user) {
        return mapToResponse(getNotificationEntity(id, user));
    }

    @Transactional
    public void markAsRead(UUID id, User user) {
        Notification notification = getNotificationEntity(id, user);
        notification.setRead(true);
        notificationRepository.save(notification);
    }

    @Transactional
    public void markAllAsRead(User user) {
        var notifications = notificationRepository.findByUserIdOrderByCreatedAtDesc(user.getId());
        notifications.forEach(n -> n.setRead(true));
        notificationRepository.saveAll(notifications);
    }

    @Transactional
    public void deleteNotification(UUID id, User user) {
        Notification notification = getNotificationEntity(id, user);
        notificationRepository.delete(notification);
    }

    @Transactional
    public void createNotification(User user, NotificationType type, String title, String message) {
        Notification notification = new Notification();
        notification.setUser(user);
        notification.setType(type);
        notification.setTitle(title);
        notification.setMessage(message);
        notification.setRead(false);
        notificationRepository.save(notification);
    }

    private Notification getNotificationEntity(UUID id, User user) {
        Notification notification = notificationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Notification not found with id: " + id));
        if (!notification.getUser().getId().equals(user.getId())) {
            throw new UnauthorizedException("You are not authorized to access this notification");
        }
        return notification;
    }

    private NotificationResponse mapToResponse(Notification notification) {
        return new NotificationResponse(
                notification.getId(),
                notification.getType().name(),
                notification.getTitle(),
                notification.getMessage(),
                notification.isRead(),
                notification.getCreatedAt()
        );
    }
}
