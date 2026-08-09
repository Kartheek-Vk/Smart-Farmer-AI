package com.smartfarmer.ai.disease.repository;

import com.smartfarmer.ai.disease.entity.DiseaseResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface DiseaseResultRepository extends JpaRepository<DiseaseResult, UUID> {
    // additional query methods if needed
}
