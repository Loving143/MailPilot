package com.email.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class RequestLoggingInterceptor implements HandlerInterceptor {

    private static final Logger logger = LoggerFactory.getLogger(RequestLoggingInterceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String requestURI = request.getRequestURI();
        String method = request.getMethod();
        
        if (requestURI.startsWith("/api/dashboard")) {
            logger.info("=== DASHBOARD REQUEST ===");
            logger.info("Method: {}", method);
            logger.info("URI: {}", requestURI);
            logger.info("Handler: {}", handler.getClass().getSimpleName());
            logger.info("Query String: {}", request.getQueryString());
            logger.info("Content Type: {}", request.getContentType());
            logger.info("========================");
        }
        
        return true;
    }
}