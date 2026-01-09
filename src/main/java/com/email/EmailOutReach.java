package com.email;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@SpringBootApplication
public class EmailOutReach {

	private static final Logger logger = LoggerFactory.getLogger(EmailOutReach.class);

	public static void main(String[] args) {
		logger.info("Starting EmailOutReach application with CORS support...");
		try {
			SpringApplication.run(EmailOutReach.class, args);
			logger.info("EmailOutReach application started successfully with CORS enabled");
		} catch (Exception e) {
			logger.error("Failed to start EmailOutReach application", e);
			throw e;
		}
	}
}
