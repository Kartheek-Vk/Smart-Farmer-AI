package com.smartfarmer.ai.admin.dto;

import com.smartfarmer.ai.common.model.BaseEntity;
import com.smartfarmer.ai.user.entity.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "audit_logs")
public class AuditLog extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "actor_user_id")
    private User actor;

    @Column(nullable = false, length = 100)
    private String action;

    @Column(length = 80)
    private String targetType;

    @Column(length = 255)
    private String targetId;

    @Column(length = 1000)
    private String details;

    @Column(length = 50)
    private String ipAddress;
}
