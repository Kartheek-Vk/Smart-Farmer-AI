package com.smartfarmer.ai.market.repository;

import com.smartfarmer.ai.market.entity.Market;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MarketRepository extends JpaRepository<Market, UUID> {
}
