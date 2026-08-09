package com.smartfarmer.ai.disease.entity;

import com.smartfarmer.ai.common.enums.DiseaseScanStatus;
import com.smartfarmer.ai.common.model.BaseEntity;
import com.smartfarmer.ai.farm.entity.Farm;
import com.smartfarmer.ai.user.entity.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "disease_scans")
public class DiseaseScan extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "farm_id")
    private Farm farm;

    @Column(nullable = false, length = 255)
    private String imageStorageKey;

    @Column(nullable = false, length = 255)
    private String imageUri;

    @Column(nullable = false, length = 150)
    private String originalFilename;

    @Column(nullable = false, length = 100)
    private String contentType;

    @Column(nullable = false)
    private long fileSize;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private DiseaseScanStatus status;
}
