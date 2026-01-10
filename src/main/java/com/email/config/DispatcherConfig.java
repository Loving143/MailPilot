package com.email.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class DispatcherConfig implements WebMvcConfigurer {

    private static final Logger logger = LoggerFactory.getLogger(DispatcherConfig.class);

    @Override
    public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
        logger.info("Configuring default servlet handling - API routes will be handled by controllers");
        // Don't enable default servlet handling to prevent static resource conflicts
        // configurer.enable(); // This is commented out intentionally
        logger.info("Default servlet handling configured - static resource handling disabled for API routes");
    }
}