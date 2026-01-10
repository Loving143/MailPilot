package com.email.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.util.Map;

@Component
public class StartupLogger implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(StartupLogger.class);

    @Autowired
    private RequestMappingHandlerMapping requestMappingHandlerMapping;

    @Override
    public void run(String... args) throws Exception {
        logger.info("=== Application Started - Registered Endpoints ===");
        
        Map<RequestMappingInfo, HandlerMethod> handlerMethods = 
            requestMappingHandlerMapping.getHandlerMethods();
        
        int dashboardEndpoints = 0;
        int totalEndpoints = 0;
        
        handlerMethods.forEach((requestMappingInfo, handlerMethod) -> {
            if (requestMappingInfo.getPathPatternsCondition() != null) {
                requestMappingInfo.getPathPatternsCondition().getPatterns().forEach(pattern -> {
                    String patternString = pattern.getPatternString();
                    if (patternString.contains("/api/dashboard")) {
                        logger.info("✓ Dashboard endpoint: {} -> {}.{}", 
                            patternString,
                            handlerMethod.getBeanType().getSimpleName(),
                            handlerMethod.getMethod().getName());
                    } else if (patternString.contains("/api/")) {
                        logger.info("✓ API endpoint: {} -> {}.{}", 
                            patternString,
                            handlerMethod.getBeanType().getSimpleName(),
                            handlerMethod.getMethod().getName());
                    }
                });
            }
        });
        
        // Count dashboard endpoints
        dashboardEndpoints = (int) handlerMethods.keySet().stream()
            .filter(info -> info.getPathPatternsCondition() != null)
            .flatMap(info -> info.getPathPatternsCondition().getPatterns().stream())
            .filter(pattern -> pattern.getPatternString().contains("/api/dashboard"))
            .count();
            
        totalEndpoints = handlerMethods.size();
        
        logger.info("=== Summary: {} total endpoints, {} dashboard endpoints ===", totalEndpoints, dashboardEndpoints);
        
        if (dashboardEndpoints == 0) {
            logger.error("❌ NO DASHBOARD ENDPOINTS REGISTERED! Check DashboardController configuration.");
        } else {
            logger.info("✅ Dashboard endpoints registered successfully");
        }
    }
}