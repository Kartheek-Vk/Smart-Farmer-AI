package com.smartfarmer.ai.recommendation.repository;

import com.smartfarmer.ai.recommendation.entity.CropRecommendation;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CropRecommendationRepository extends JpaRepository<CropRecommendation, UUID> {

    List<CropRecommendation> findByUserIdOrderByCreatedAtDesc(UUID userId);
}
