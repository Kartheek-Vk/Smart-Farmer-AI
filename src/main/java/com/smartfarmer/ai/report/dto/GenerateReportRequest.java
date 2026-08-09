package com.smartfarmer.ai.report.dto;

import com.smartfarmer.ai.common.enums.ReportType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.UUID;

public record GenerateReportRequest(
        @NotNull(message = "Report type is required")
        ReportType reportType,

        UUID farmId,

        @Size(max = 150, message = "Title must not exceed 150 characters")
        String title
) {
}
