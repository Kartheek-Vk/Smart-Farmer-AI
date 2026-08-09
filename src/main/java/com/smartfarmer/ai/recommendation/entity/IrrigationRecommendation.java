package com.smartfarmer.ai.recommendation.entity;

import com.smartfarmer.ai.common.enums.RecommendationStatus;
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
@Table(name = "irrigation_recommendations")
public class IrrigationRecommendation extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "farm_id")
    private Farm farm;

    @Column(nullable = false, length = 255)
    private String inputSummary;

    @Column(length = 2000)
    private String recommendationText;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private RecommendationStatus status;
}
