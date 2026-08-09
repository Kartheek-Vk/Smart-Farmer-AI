package com.smartfarmer.ai.admin.dto;

public record SystemStatsResponse(
        long totalUsers,
        long totalFarms,
        long totalDiseaseScans,
        long totalConversations
) {
}
