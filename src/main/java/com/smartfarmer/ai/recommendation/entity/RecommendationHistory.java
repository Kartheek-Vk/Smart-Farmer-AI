package com.smartfarmer.ai.recommendation.entity;

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
@Table(name = "recommendation_history")
public class RecommendationHistory extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false, length = 60)
    private String recommendationType;

    @Column(nullable = false, length = 255)
    private String referenceId;

    @Column(length = 500)
    private String summary;
}
