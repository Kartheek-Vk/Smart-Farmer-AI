package com.smartfarmer.ai.recommendation.repository;

import com.smartfarmer.ai.recommendation.entity.FertilizerRecommendation;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FertilizerRecommendationRepository extends JpaRepository<FertilizerRecommendation, UUID> {

    List<FertilizerRecommendation> findByUserIdOrderByCreatedAtDesc(UUID userId);
}
