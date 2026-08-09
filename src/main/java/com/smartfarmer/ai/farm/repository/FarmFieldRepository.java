package com.smartfarmer.ai.farm.repository;

import com.smartfarmer.ai.farm.entity.FarmField;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface FarmFieldRepository extends JpaRepository<FarmField, UUID> {
    List<FarmField> findByFarmId(UUID farmId);
    Page<FarmField> findByFarmId(UUID farmId, Pageable pageable);
}
