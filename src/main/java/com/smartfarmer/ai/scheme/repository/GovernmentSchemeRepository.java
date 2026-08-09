package com.smartfarmer.ai.scheme.repository;

import com.smartfarmer.ai.scheme.entity.GovernmentScheme;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GovernmentSchemeRepository extends JpaRepository<GovernmentScheme, UUID> {

    List<GovernmentScheme> findByActiveTrueOrderByUpdatedAtDesc();

    org.springframework.data.domain.Page<GovernmentScheme> findByCategoryAndStateAndActive(String category, String state, Boolean active, org.springframework.data.domain.Pageable pageable);
    org.springframework.data.domain.Page<GovernmentScheme> findByCategoryAndActive(String category, Boolean active, org.springframework.data.domain.Pageable pageable);
    org.springframework.data.domain.Page<GovernmentScheme> findByStateAndActive(String state, Boolean active, org.springframework.data.domain.Pageable pageable);
    org.springframework.data.domain.Page<GovernmentScheme> findByActive(Boolean active, org.springframework.data.domain.Pageable pageable);
    
    @org.springframework.data.jpa.repository.Query("SELECT s FROM GovernmentScheme s WHERE LOWER(s.title) LIKE LOWER(CONCAT('%', :query, '%')) OR LOWER(s.description) LIKE LOWER(CONCAT('%', :query, '%'))")
    org.springframework.data.domain.Page<GovernmentScheme> searchSchemes(@org.springframework.data.repository.query.Param("query") String query, org.springframework.data.domain.Pageable pageable);
}
