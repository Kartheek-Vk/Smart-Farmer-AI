package com.smartfarmer.ai.disease.repository;

import com.smartfarmer.ai.disease.entity.DiseaseScan;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DiseaseScanRepository extends JpaRepository<DiseaseScan, UUID> {

    Page<DiseaseScan> findByUserId(UUID userId, Pageable pageable);

    long countByUserId(UUID userId);

    long countByUserIdAndFarmId(UUID userId, UUID farmId);

    Page<DiseaseScan> findByUserIdAndFarmId(UUID userId, UUID farmId, Pageable pageable);
}
