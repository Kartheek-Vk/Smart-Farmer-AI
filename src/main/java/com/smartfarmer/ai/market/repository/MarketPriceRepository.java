package com.smartfarmer.ai.market.repository;

import com.smartfarmer.ai.market.entity.MarketPrice;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MarketPriceRepository extends JpaRepository<MarketPrice, UUID> {

    Page<MarketPrice> findByMarketId(UUID marketId, Pageable pageable);

    Page<MarketPrice> findByMarketIdAndCropId(UUID marketId, UUID cropId, Pageable pageable);

    Page<MarketPrice> findByCropId(UUID cropId, Pageable pageable);

    List<MarketPrice> findByMarketIdAndCropIdAndPriceDateBetweenOrderByPriceDateAsc(
            UUID marketId, UUID cropId, LocalDate from, LocalDate to);
}
