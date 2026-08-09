package com.smartfarmer.ai.recommendation.repository;

import com.smartfarmer.ai.recommendation.entity.IrrigationRecommendation;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IrrigationRecommendationRepository extends JpaRepository<IrrigationRecommendation, UUID> {

    List<IrrigationRecommendation> findByUserIdOrderByCreatedAtDesc(UUID userId);
}
