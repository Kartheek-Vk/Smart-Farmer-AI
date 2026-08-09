package com.smartfarmer.ai.recommendation.repository;

import com.smartfarmer.ai.recommendation.entity.RecommendationHistory;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RecommendationHistoryRepository extends JpaRepository<RecommendationHistory, UUID> {

    Page<RecommendationHistory> findByUserId(UUID userId, Pageable pageable);

    long countByUserId(UUID userId);
}
