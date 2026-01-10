package com.email.service;

import com.email.resposne.DashboardStatsResponse;
import com.email.resposne.DetailedStatsResponse;

public interface DashboardService {
    
    /**
     * Get dashboard statistics for the current user
     */
    DashboardStatsResponse getDashboardStats();
    
    /**
     * Get detailed statistics for the current user
     */
    DetailedStatsResponse getDetailedStats();
    
    /**
     * Get dashboard statistics for a specific date range
     */
    DashboardStatsResponse getDashboardStatsByDateRange(String startDate, String endDate);
    
    /**
     * Get statistics for a specific period (today, week, month, year)
     */
    DetailedStatsResponse getStatsByPeriod(String period);
}