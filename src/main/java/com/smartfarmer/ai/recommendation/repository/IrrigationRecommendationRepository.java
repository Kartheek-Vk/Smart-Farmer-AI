package com.smartfarmer.ai.recommendation.repository;

import com.smartfarmer.ai.recommendation.entity.IrrigationRecommendation;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IrrigationRecommendationRepository extends JpaRepository<IrrigationRecommendation, UUID> {

    Page<IrrigationRecommendation> findByUserId(UUID userId, Pageable pageable);
}
