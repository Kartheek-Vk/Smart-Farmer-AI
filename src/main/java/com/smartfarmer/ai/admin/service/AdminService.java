package com.smartfarmer.ai.admin.service;

import com.smartfarmer.ai.admin.dto.AdminUserResponse;
import com.smartfarmer.ai.admin.dto.BroadcastNotificationRequest;
import com.smartfarmer.ai.admin.dto.SystemStatsResponse;
import com.smartfarmer.ai.assistant.repository.AIConversationRepository;
import com.smartfarmer.ai.common.enums.UserStatus;
import com.smartfarmer.ai.disease.repository.DiseaseScanRepository;
import com.smartfarmer.ai.exception.ResourceNotFoundException;
import com.smartfarmer.ai.farm.repository.FarmRepository;
import com.smartfarmer.ai.notification.service.NotificationService;
import java.util.List;
import com.smartfarmer.ai.user.entity.User;
import com.smartfarmer.ai.user.repository.UserRepository;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AdminService {

    private final UserRepository userRepository;
    private final FarmRepository farmRepository;
    private final DiseaseScanRepository diseaseScanRepository;
    private final AIConversationRepository aiConversationRepository;
    private final AuditLogService auditLogService;
    private final NotificationService notificationService;

    public AdminService(UserRepository userRepository,
                        FarmRepository farmRepository,
                        DiseaseScanRepository diseaseScanRepository,
                        AIConversationRepository aiConversationRepository,
                        AuditLogService auditLogService,
                        NotificationService notificationService) {
        this.userRepository = userRepository;
        this.farmRepository = farmRepository;
        this.diseaseScanRepository = diseaseScanRepository;
        this.aiConversationRepository = aiConversationRepository;
        this.auditLogService = auditLogService;
        this.notificationService = notificationService;
    }

    @Transactional(readOnly = true)
    public Page<AdminUserResponse> listUsers(Pageable pageable) {
        return userRepository.findAll(pageable)
                .map(this::mapToAdminUserResponse);
    }

    @Transactional(readOnly = true)
    public AdminUserResponse getUserById(UUID id) {
        return userRepository.findById(id)
                .map(this::mapToAdminUserResponse)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
    }

    @Transactional
    public void activateUser(UUID id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
        user.setStatus(UserStatus.ACTIVE);
        userRepository.save(user);
        auditLogService.log("ACTIVATE_USER", "USER", id.toString(), "User activated by admin");
    }

    @Transactional
    public void deactivateUser(UUID id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
        user.setStatus(UserStatus.INACTIVE);
        userRepository.save(user);
        auditLogService.log("DEACTIVATE_USER", "USER", id.toString(), "User deactivated by admin");
    }

    @Transactional(readOnly = true)
    public SystemStatsResponse getStats() {
        return new SystemStatsResponse(
                userRepository.count(),
                farmRepository.count(),
                diseaseScanRepository.count(),
                aiConversationRepository.count()
        );
    }

    /**
     * Sends a notification to a single user, or to every user when no recipient is given.
     */
    @Transactional
    public int sendNotification(BroadcastNotificationRequest request) {
        List<User> recipients = request.userId() == null
                ? userRepository.findAll()
                : List.of(userRepository.findById(request.userId())
                        .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + request.userId())));
        recipients.forEach(recipient -> notificationService.createNotification(
                recipient, request.type(), request.title(), request.message()));
        auditLogService.log("SEND_NOTIFICATION", "NOTIFICATION",
                request.userId() != null ? request.userId().toString() : "ALL", request.title());
        return recipients.size();
    }

    private AdminUserResponse mapToAdminUserResponse(User user) {
        return new AdminUserResponse(
                user.getId(),
                user.getEmail(),
                user.getFirstName(),
                user.getLastName(),
                user.getStatus().name(),
                user.getRolesAsEnumSet().stream().map(Enum::name).collect(Collectors.toSet()),
                user.getCreatedAt(),
                user.getUpdatedAt()
        );
    }
}
