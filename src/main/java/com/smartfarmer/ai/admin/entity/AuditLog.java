package com.smartfarmer.ai.admin.entity;

import com.smartfarmer.ai.common.model.BaseEntity;
import com.smartfarmer.ai.user.entity.User;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "audit_logs")
@Getter
@Setter
public class AuditLog extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "actor_user_id")
    private User actor;

    private String action;
    private String targetType;
    private String targetId;
    private String details;
    private String ipAddress;
}
