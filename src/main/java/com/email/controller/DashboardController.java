package com.email.controller;

import com.email.exception.ApiResponse;
import com.email.service.DashboardService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {

    private static final Logger logger = LoggerFactory.getLogger(DashboardController.class);

    @Autowired
    private DashboardService dashboardService;

    public DashboardController() {
        logger.info("DashboardController initialized");
    }

    @GetMapping("/test")
    public ResponseEntity<?> testEndpoint() {
        logger.info("Dashboard test endpoint called");
        return ResponseEntity.ok(Map.of("status", "success", "message", "Dashboard controller is working"));
    }

    @GetMapping("/health")
    public ResponseEntity<?> healthCheck() {
        logger.info("Dashboard health check called");
        try {
            // Simple test to see if service is working
            String currentUsername = (String) SecurityContextHolder.getContext().getAuthentication().getName();
            logger.info("Current user: {}", currentUsername);
            
            return ResponseEntity.ok(Map.of(
                "status", "healthy", 
                "message", "Dashboard service is operational",
                "user", currentUsername
            ));
        } catch (Exception e) {
            logger.error("Dashboard health check failed", e);
            return ResponseEntity.ok(Map.of(
                "status", "error", 
                "message", e.getMessage()
            ));
        }
    }

    @GetMapping("/ping")
    public ResponseEntity<?> ping() {
        logger.info("Dashboard ping endpoint called");
        return ResponseEntity.ok(Map.of(
            "status", "pong",
            "timestamp", System.currentTimeMillis(),
            "controller", "DashboardController"
        ));
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/stats")
    public ResponseEntity<?> getDashboardStats() {
        logger.info("Dashboard stats request received");
        try {
            var stats = dashboardService.getDashboardStats();
            logger.info("Dashboard stats retrieved successfully");
            return ResponseEntity.ok(new ApiResponse("1", "Dashboard stats retrieved successfully", stats));
        } catch (Exception e) {
            logger.error("Error retrieving dashboard stats", e);
            throw e;
        }
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/stats/detailed")
    public ResponseEntity<?> getDetailedStats() {
        logger.info("Detailed stats request received");
        try {
            var stats = dashboardService.getDetailedStats();
            logger.info("Detailed stats retrieved successfully");
            return ResponseEntity.ok(new ApiResponse("1", "Detailed stats retrieved successfully", stats));
        } catch (Exception e) {
            logger.error("Error retrieving detailed stats", e);
            throw e;
        }
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/stats/range")
    public ResponseEntity<?> getDashboardStatsByDateRange(
            @RequestParam String startDate,
            @RequestParam String endDate) {
        logger.info("Dashboard stats by date range request received: {} to {}", startDate, endDate);
        try {
            var stats = dashboardService.getDashboardStatsByDateRange(startDate, endDate);
            logger.info("Dashboard stats by date range retrieved successfully");
            return ResponseEntity.ok(new ApiResponse("1", "Dashboard stats by date range retrieved successfully", stats));
        } catch (Exception e) {
            logger.error("Error retrieving dashboard stats by date range", e);
            throw e;
        }
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/stats/period/{period}")
    public ResponseEntity<?> getStatsByPeriod(@PathVariable String period) {
        logger.info("Stats by period request received: {}", period);
        try {
            var stats = dashboardService.getStatsByPeriod(period);
            logger.info("Stats by period retrieved successfully for: {}", period);
            return ResponseEntity.ok(new ApiResponse("1", "Stats by period retrieved successfully", stats));
        } catch (Exception e) {
            logger.error("Error retrieving stats by period: {}", period, e);
            throw e;
        }
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/summary")
    public ResponseEntity<?> getDashboardSummary() {
        logger.info("Dashboard summary request received");
        try {
            var dashboardStats = dashboardService.getDashboardStats();
            var detailedStats = dashboardService.getDetailedStats();
            
            var summary = Map.of(
                "dashboard", dashboardStats,
                "detailed", detailedStats
            );
            
            logger.info("Dashboard summary retrieved successfully");
            return ResponseEntity.ok(new ApiResponse("1", "Dashboard summary retrieved successfully", summary));
        } catch (Exception e) {
            logger.error("Error retrieving dashboard summary", e);
            throw e;
        }
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/quick-stats")
    public ResponseEntity<?> getQuickStats() {
        logger.info("Quick stats request received");
        try {
            var stats = dashboardService.getDashboardStats();
            
            // Return simplified stats for quick display
            var quickStats = Map.of(
                "totalEmails", stats.getTotalEmails(),
                "sentToday", stats.getSentToday(),
                "responseRate", String.format("%.1f%%", stats.getResponseRate()),
                "activeCampaigns", stats.getActiveCampaigns()
            );
            
            logger.info("Quick stats retrieved successfully");
            return ResponseEntity.ok(new ApiResponse("1", "Quick stats retrieved successfully", quickStats));
        } catch (Exception e) {
            logger.error("Error retrieving quick stats", e);
            throw e;
        }
    }
}