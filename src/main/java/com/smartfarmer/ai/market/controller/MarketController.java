package com.smartfarmer.ai.market.controller;

import com.smartfarmer.ai.common.api.ApiResponse;
import com.smartfarmer.ai.common.api.ApiResponseFactory;
import com.smartfarmer.ai.common.api.PageResponse;
import com.smartfarmer.ai.market.dto.MarketPriceResponse;
import com.smartfarmer.ai.market.dto.MarketPriceTrendResponse;
import com.smartfarmer.ai.market.dto.MarketResponse;
import com.smartfarmer.ai.market.service.MarketService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.util.UUID;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Markets", description = "Market directory, prices and price trends")
@RestController
@RequestMapping("/api/v1/markets")
public class MarketController {

    private final MarketService marketService;
    private final ApiResponseFactory apiResponseFactory;

    public MarketController(MarketService marketService, ApiResponseFactory apiResponseFactory) {
        this.marketService = marketService;
        this.apiResponseFactory = apiResponseFactory;
    }

    @GetMapping
    public ApiResponse<PageResponse<MarketResponse>> listMarkets(@RequestParam(required = false) String state,
                                                                 @RequestParam(required = false, name = "q") String query,
                                                                 Pageable pageable,
                                                                 HttpServletRequest request) {
        return apiResponseFactory.page("Markets retrieved successfully",
                marketService.listMarkets(state, query, pageable), request);
    }

    @GetMapping("/{id}")
    public ApiResponse<MarketResponse> getMarket(@PathVariable UUID id, HttpServletRequest request) {
        return apiResponseFactory.success("Market retrieved successfully", marketService.getMarketById(id), request);
    }

    @GetMapping("/prices")
    public ApiResponse<PageResponse<MarketPriceResponse>> listPrices(@RequestParam(required = false) UUID marketId,
                                                                     @RequestParam(required = false) UUID cropId,
                                                                     Pageable pageable,
                                                                     HttpServletRequest request) {
        return apiResponseFactory.page("Market prices retrieved successfully",
                marketService.listPrices(marketId, cropId, pageable), request);
    }

    @GetMapping("/{id}/prices")
    public ApiResponse<PageResponse<MarketPriceResponse>> listPricesForMarket(@PathVariable UUID id,
                                                                              @RequestParam(required = false) UUID cropId,
                                                                              Pageable pageable,
                                                                              HttpServletRequest request) {
        return apiResponseFactory.page("Market prices retrieved successfully",
                marketService.listPrices(id, cropId, pageable), request);
    }

    @GetMapping("/{id}/prices/trend")
    public ApiResponse<MarketPriceTrendResponse> getPriceTrend(
            @PathVariable UUID id,
            @RequestParam UUID cropId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to,
            HttpServletRequest request) {
        return apiResponseFactory.success("Market price trend retrieved successfully",
                marketService.getPriceTrend(id, cropId, from, to), request);
    }
}
