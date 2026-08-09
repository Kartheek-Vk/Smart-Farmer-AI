package com.smartfarmer.ai.report.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.smartfarmer.ai.common.enums.ReportType;
import com.smartfarmer.ai.crop.repository.CropSeasonRepository;
import com.smartfarmer.ai.disease.repository.DiseaseScanRepository;
import com.smartfarmer.ai.exception.ResourceNotFoundException;
import com.smartfarmer.ai.farm.entity.Farm;
import com.smartfarmer.ai.farm.repository.FarmRepository;
import com.smartfarmer.ai.market.repository.MarketPriceRepository;
import com.smartfarmer.ai.recommendation.repository.RecommendationHistoryRepository;
import com.smartfarmer.ai.report.dto.GenerateReportRequest;
import com.smartfarmer.ai.report.dto.ReportResponse;
import com.smartfarmer.ai.report.entity.Report;
import com.smartfarmer.ai.report.repository.ReportRepository;
import com.smartfarmer.ai.user.entity.User;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service
public class ReportService {

    private static final String STATUS_COMPLETED = "COMPLETED";

    private final ReportRepository reportRepository;
    private final FarmRepository farmRepository;
    private final CropSeasonRepository cropSeasonRepository;
    private final DiseaseScanRepository diseaseScanRepository;
    private final RecommendationHistoryRepository recommendationHistoryRepository;
    private final MarketPriceRepository marketPriceRepository;
    private final ObjectMapper objectMapper;

    public ReportService(ReportRepository reportRepository,
                         FarmRepository farmRepository,
                         CropSeasonRepository cropSeasonRepository,
                         DiseaseScanRepository diseaseScanRepository,
                         RecommendationHistoryRepository recommendationHistoryRepository,
                         MarketPriceRepository marketPriceRepository,
                         ObjectMapper objectMapper) {
        this.reportRepository = reportRepository;
        this.farmRepository = farmRepository;
        this.cropSeasonRepository = cropSeasonRepository;
        this.diseaseScanRepository = diseaseScanRepository;
        this.recommendationHistoryRepository = recommendationHistoryRepository;
        this.marketPriceRepository = marketPriceRepository;
        this.objectMapper = objectMapper;
    }

    @Transactional
    public ReportResponse generateReport(GenerateReportRequest request, User user) {
        Farm farm = null;
        if (request.farmId() != null) {
            farm = farmRepository.findById(request.farmId())
                    .orElseThrow(() -> new ResourceNotFoundException("Farm not found with id: " + request.farmId()));
            if (!farm.getOwner().getId().equals(user.getId())) {
                throw new AccessDeniedException("You do not have permission to access this farm");
            }
        }

        Report report = new Report();
        report.setUser(user);
        report.setFarm(farm);
        report.setReportType(request.reportType());
        report.setTitle(StringUtils.hasText(request.title())
                ? request.title()
                : defaultTitle(request.reportType(), farm));
        report.setStatus(STATUS_COMPLETED);
        report.setMetadataJson(writeJson(collectMetrics(request.reportType(), user, farm)));

        return mapToResponse(reportRepository.save(report));
    }

    @Transactional(readOnly = true)
    public Page<ReportResponse> listReports(User user, Pageable pageable) {
        return reportRepository.findByUserId(user.getId(), pageable)
                .map(this::mapToResponse);
    }

    @Transactional(readOnly = true)
    public ReportResponse getReportById(UUID id, User user) {
        Report report = reportRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Report not found with id: " + id));

        if (!report.getUser().getId().equals(user.getId())) {
            throw new AccessDeniedException("You do not have access to this report");
        }

        return mapToResponse(report);
    }

    @Transactional
    public void deleteReport(UUID id, User user) {
        Report report = reportRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Report not found with id: " + id));

        if (!report.getUser().getId().equals(user.getId())) {
            throw new AccessDeniedException("You do not have access to this report");
        }

        reportRepository.delete(report);
    }

    private Map<String, Object> collectMetrics(ReportType reportType, User user, Farm farm) {
        Map<String, Object> metrics = new LinkedHashMap<>();
        switch (reportType) {
            case FARM_SUMMARY -> {
                metrics.put("farms", farmRepository.countByOwnerId(user.getId()));
                metrics.put("cropSeasons", farm == null
                        ? cropSeasonRepository.countByOwnerId(user.getId())
                        : cropSeasonRepository.countByOwnerIdAndFarmId(user.getId(), farm.getId()));
                if (farm != null) {
                    metrics.put("farmName", farm.getName());
                    metrics.put("area", farm.getArea());
                    metrics.put("areaUnit", farm.getAreaUnit().name());
                }
            }
            case DISEASE_HISTORY -> metrics.put("diseaseScans", farm == null
                    ? diseaseScanRepository.countByUserId(user.getId())
                    : diseaseScanRepository.countByUserIdAndFarmId(user.getId(), farm.getId()));
            case RECOMMENDATION_HISTORY ->
                    metrics.put("recommendations", recommendationHistoryRepository.countByUserId(user.getId()));
            case MARKET_TREND -> {
                metrics.put("cropSeasons", cropSeasonRepository.countByOwnerId(user.getId()));
                metrics.put("marketPriceRecords", marketPriceRepository.count());
            }
        }
        return metrics;
    }

    private String defaultTitle(ReportType reportType, Farm farm) {
        String base = reportType.name().replace('_', ' ').toLowerCase();
        return farm == null ? "Report: " + base : "Report: " + base + " for " + farm.getName();
    }

    private String writeJson(Map<String, Object> metrics) {
        try {
            return objectMapper.writeValueAsString(metrics);
        } catch (JsonProcessingException ex) {
            throw new IllegalStateException("Unable to serialise report metrics", ex);
        }
    }

    private ReportResponse mapToResponse(Report report) {
        return new ReportResponse(
                report.getId(),
                report.getFarm() != null ? report.getFarm().getId() : null,
                report.getReportType().name(),
                report.getTitle(),
                report.getStatus(),
                report.getMetadataJson(),
                report.getCreatedAt()
        );
    }
}
