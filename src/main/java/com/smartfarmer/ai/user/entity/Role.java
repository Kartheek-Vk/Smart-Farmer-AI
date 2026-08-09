package com.smartfarmer.ai.user.entity;

import com.smartfarmer.ai.common.enums.UserRole;
import com.smartfarmer.ai.common.model.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "roles")
public class Role extends BaseEntity {

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, unique = true, length = 30)
    private UserRole name;

    @Column(nullable = false, length = 120)
    private String description;
}
