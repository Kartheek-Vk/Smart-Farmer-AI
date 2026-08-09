package com.smartfarmer.ai.report.repository;

import com.smartfarmer.ai.report.entity.Report;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReportRepository extends JpaRepository<Report, UUID> {

    List<Report> findByUserIdOrderByCreatedAtDesc(UUID userId);
    
    org.springframework.data.domain.Page<Report> findByUserId(UUID userId, org.springframework.data.domain.Pageable pageable);
}
