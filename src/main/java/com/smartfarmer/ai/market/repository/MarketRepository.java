package com.smartfarmer.ai.market.repository;

import com.smartfarmer.ai.market.entity.Market;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MarketRepository extends JpaRepository<Market, UUID> {

    Page<Market> findByStateIgnoreCase(String state, Pageable pageable);

    Optional<Market> findByNameIgnoreCaseAndStateIgnoreCase(String name, String state);

    @Query("SELECT m FROM Market m WHERE LOWER(m.name) LIKE LOWER(CONCAT('%', :query, '%')) "
            + "OR LOWER(m.location) LIKE LOWER(CONCAT('%', :query, '%'))")
    Page<Market> search(@Param("query") String query, Pageable pageable);
}
