package com.smartfarmer.ai.admin.service;

import com.smartfarmer.ai.admin.entity.AuditLog;
import com.smartfarmer.ai.admin.repository.AuditLogRepository;
import com.smartfarmer.ai.security.CurrentUserService;
import com.smartfarmer.ai.user.entity.User;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Service
public class AuditLogService {

    private final AuditLogRepository auditLogRepository;
    private final CurrentUserService currentUserService;

    public AuditLogService(AuditLogRepository auditLogRepository, CurrentUserService currentUserService) {
        this.auditLogRepository = auditLogRepository;
        this.currentUserService = currentUserService;
    }

    @Transactional
    public void log(String action, String targetType, String targetId, String details) {
        AuditLog entry = new AuditLog();
        try {
            User actor = currentUserService.currentUser();
            entry.setActor(actor);
        } catch (RuntimeException ignored) {
            entry.setActor(null);
        }
        entry.setAction(action);
        entry.setTargetType(targetType);
        entry.setTargetId(targetId);
        entry.setDetails(details);
        entry.setIpAddress(resolveClientIp());
        auditLogRepository.save(entry);
    }

    @Transactional(readOnly = true)
    public List<AuditLog> latest() {
        return auditLogRepository.findTop100ByOrderByCreatedAtDesc();
    }

    private String resolveClientIp() {
        if (!(RequestContextHolder.getRequestAttributes() instanceof ServletRequestAttributes attributes)) {
            return null;
        }
        return attributes.getRequest().getRemoteAddr();
    }
}
