package com.smartfarmer.ai.market.service;

import com.smartfarmer.ai.crop.entity.Crop;
import com.smartfarmer.ai.crop.repository.CropRepository;
import com.smartfarmer.ai.exception.DuplicateResourceException;
import com.smartfarmer.ai.exception.ResourceNotFoundException;
import com.smartfarmer.ai.market.dto.CreateMarketPriceRequest;
import com.smartfarmer.ai.market.dto.CreateMarketRequest;
import com.smartfarmer.ai.market.dto.MarketPriceResponse;
import com.smartfarmer.ai.market.dto.MarketPriceTrendResponse;
import com.smartfarmer.ai.market.dto.MarketResponse;
import com.smartfarmer.ai.market.entity.Market;
import com.smartfarmer.ai.market.entity.MarketPrice;
import com.smartfarmer.ai.market.repository.MarketPriceRepository;
import com.smartfarmer.ai.market.repository.MarketRepository;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service
public class MarketService {

    private final MarketRepository marketRepository;
    private final MarketPriceRepository marketPriceRepository;
    private final CropRepository cropRepository;

    public MarketService(MarketRepository marketRepository,
                         MarketPriceRepository marketPriceRepository,
                         CropRepository cropRepository) {
        this.marketRepository = marketRepository;
        this.marketPriceRepository = marketPriceRepository;
        this.cropRepository = cropRepository;
    }

    @Transactional(readOnly = true)
    public Page<MarketResponse> listMarkets(String state, String query, Pageable pageable) {
        Page<Market> markets;
        if (StringUtils.hasText(query)) {
            markets = marketRepository.search(query, pageable);
        } else if (StringUtils.hasText(state)) {
            markets = marketRepository.findByStateIgnoreCase(state, pageable);
        } else {
            markets = marketRepository.findAll(pageable);
        }
        return markets.map(this::mapToResponse);
    }

    @Transactional(readOnly = true)
    public MarketResponse getMarketById(UUID id) {
        return mapToResponse(getMarketEntity(id));
    }

    @Transactional(readOnly = true)
    public Page<MarketPriceResponse> listPrices(UUID marketId, UUID cropId, Pageable pageable) {
        Page<MarketPrice> prices;
        if (marketId != null && cropId != null) {
            prices = marketPriceRepository.findByMarketIdAndCropId(marketId, cropId, pageable);
        } else if (marketId != null) {
            prices = marketPriceRepository.findByMarketId(marketId, pageable);
        } else if (cropId != null) {
            prices = marketPriceRepository.findByCropId(cropId, pageable);
        } else {
            prices = marketPriceRepository.findAll(pageable);
        }
        return prices.map(this::mapToResponse);
    }

    @Transactional(readOnly = true)
    public MarketPriceTrendResponse getPriceTrend(UUID marketId, UUID cropId, LocalDate from, LocalDate to) {
        getMarketEntity(marketId);
        LocalDate end = to != null ? to : LocalDate.now();
        LocalDate start = from != null ? from : end.minusMonths(3);
        List<MarketPrice> prices = marketPriceRepository
                .findByMarketIdAndCropIdAndPriceDateBetweenOrderByPriceDateAsc(marketId, cropId, start, end);

        List<MarketPriceTrendResponse.MarketPricePoint> points = prices.stream()
                .map(price -> new MarketPriceTrendResponse.MarketPricePoint(
                        price.getPriceDate(), price.getPrice(), price.getUnit()))
                .toList();

        BigDecimal min = prices.stream().map(MarketPrice::getPrice).min(Comparator.naturalOrder()).orElse(null);
        BigDecimal max = prices.stream().map(MarketPrice::getPrice).max(Comparator.naturalOrder()).orElse(null);
        BigDecimal average = prices.isEmpty() ? null : prices.stream()
                .map(MarketPrice::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .divide(BigDecimal.valueOf(prices.size()), 2, RoundingMode.HALF_UP);

        return new MarketPriceTrendResponse(marketId, cropId, start, end, min, max, average, points);
    }

    @Transactional
    public MarketResponse createMarket(CreateMarketRequest request) {
        marketRepository.findByNameIgnoreCaseAndStateIgnoreCase(request.name(), request.state())
                .ifPresent(existing -> {
                    throw new DuplicateResourceException("A market with this name already exists in this state");
                });
        Market market = new Market();
        market.setName(request.name());
        market.setLocation(request.location());
        market.setState(request.state());
        return mapToResponse(marketRepository.save(market));
    }

    @Transactional
    public MarketPriceResponse addPrice(UUID marketId, CreateMarketPriceRequest request) {
        Market market = getMarketEntity(marketId);
        Crop crop = cropRepository.findById(request.cropId())
                .orElseThrow(() -> new ResourceNotFoundException("Crop not found with id: " + request.cropId()));

        MarketPrice price = new MarketPrice();
        price.setMarket(market);
        price.setCrop(crop);
        price.setPrice(request.price());
        price.setUnit(request.unit());
        price.setPriceDate(request.priceDate());
        return mapToResponse(marketPriceRepository.save(price));
    }

    @Transactional
    public void deletePrice(UUID priceId) {
        MarketPrice price = marketPriceRepository.findById(priceId)
                .orElseThrow(() -> new ResourceNotFoundException("Market price not found with id: " + priceId));
        marketPriceRepository.delete(price);
    }

    private Market getMarketEntity(UUID id) {
        return marketRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Market not found with id: " + id));
    }

    private MarketResponse mapToResponse(Market market) {
        return new MarketResponse(market.getId(), market.getName(), market.getLocation(),
                market.getState(), market.getCreatedAt());
    }

    private MarketPriceResponse mapToResponse(MarketPrice price) {
        return new MarketPriceResponse(
                price.getId(),
                price.getMarket().getId(),
                price.getMarket().getName(),
                price.getCrop().getId(),
                price.getCrop().getName(),
                price.getPrice(),
                price.getUnit(),
                price.getPriceDate(),
                price.getCreatedAt());
    }
}
