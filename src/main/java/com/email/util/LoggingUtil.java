package com.email.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Utility class for common logging operations
 */
public class LoggingUtil {
    
    private static final Logger logger = LoggerFactory.getLogger(LoggingUtil.class);
    
    /**
     * Log method entry with parameters
     */
    public static void logMethodEntry(Logger logger, String methodName, Object... params) {
        if (logger.isDebugEnabled()) {
            StringBuilder sb = new StringBuilder();
            sb.append("Entering method: ").append(methodName);
            if (params.length > 0) {
                sb.append(" with parameters: ");
                for (int i = 0; i < params.length; i++) {
                    if (i > 0) sb.append(", ");
                    sb.append(params[i]);
                }
            }
            logger.debug(sb.toString());
        }
    }
    
    /**
     * Log method exit
     */
    public static void logMethodExit(Logger logger, String methodName) {
        if (logger.isDebugEnabled()) {
            logger.debug("Exiting method: {}", methodName);
        }
    }
    
    /**
     * Log method exit with return value
     */
    public static void logMethodExit(Logger logger, String methodName, Object returnValue) {
        if (logger.isDebugEnabled()) {
            logger.debug("Exiting method: {} with return value: {}", methodName, returnValue);
        }
    }
    
    /**
     * Log performance metrics
     */
    public static void logPerformance(Logger logger, String operation, long startTime) {
        long duration = System.currentTimeMillis() - startTime;
        logger.info("Performance: {} completed in {} ms", operation, duration);
    }
    
    /**
     * Log security events
     */
    public static void logSecurityEvent(Logger logger, String event, String user, String details) {
        logger.warn("SECURITY EVENT: {} - User: {} - Details: {}", event, user, details);
    }
    
    /**
     * Log business events
     */
    public static void logBusinessEvent(Logger logger, String event, String details) {
        logger.info("BUSINESS EVENT: {} - {}", event, details);
    }
}