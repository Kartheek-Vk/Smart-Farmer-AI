package com.smartfarmer.ai.report.service;

import com.smartfarmer.ai.exception.ResourceNotFoundException;
import com.smartfarmer.ai.report.dto.ReportResponse;
import com.smartfarmer.ai.report.entity.Report;
import com.smartfarmer.ai.report.repository.ReportRepository;
import com.smartfarmer.ai.user.entity.User;
import org.springframework.security.access.AccessDeniedException;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ReportService {

    private final ReportRepository reportRepository;

    public ReportService(ReportRepository reportRepository) {
        this.reportRepository = reportRepository;
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
