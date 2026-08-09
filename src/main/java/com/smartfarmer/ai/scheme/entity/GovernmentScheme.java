package com.smartfarmer.ai.scheme.entity;

import com.smartfarmer.ai.common.model.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "government_schemes")
public class GovernmentScheme extends BaseEntity {

    @Column(nullable = false, length = 160)
    private String title;

    @Column(nullable = false, length = 80)
    private String category;

    @Column(nullable = false, length = 120)
    private String state;

    @Column(nullable = false, length = 1000)
    private String eligibility;

    @Column(nullable = false)
    private boolean active;

    @Column(length = 2000)
    private String description;
}
