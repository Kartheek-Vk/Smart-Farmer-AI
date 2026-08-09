package com.smartfarmer.ai.disease.repository;

import com.smartfarmer.ai.disease.entity.DiseaseResult;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DiseaseResultRepository extends JpaRepository<DiseaseResult, UUID> {

    List<DiseaseResult> findByDiseaseScanId(UUID diseaseScanId);
}
