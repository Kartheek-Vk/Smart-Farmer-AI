package com.smartfarmer.ai.recommendation.repository;

import com.smartfarmer.ai.recommendation.entity.RecommendationHistory;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecommendationHistoryRepository extends JpaRepository<RecommendationHistory, UUID> {

    List<RecommendationHistory> findByUserIdOrderByCreatedAtDesc(UUID userId);
}
