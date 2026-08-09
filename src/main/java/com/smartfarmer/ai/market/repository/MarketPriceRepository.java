package com.smartfarmer.ai.market.repository;

import com.smartfarmer.ai.market.entity.MarketPrice;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MarketPriceRepository extends JpaRepository<MarketPrice, UUID> {

    List<MarketPrice> findByMarketIdOrderByPriceDateDesc(UUID marketId);
}
