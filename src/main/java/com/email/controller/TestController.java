package com.email.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/test")
public class TestController {

    private static final Logger logger = LoggerFactory.getLogger(TestController.class);

    public TestController() {
        logger.info("TestController initialized");
    }

    @GetMapping("/hello")
    public ResponseEntity<?> hello() {
        logger.info("Test hello endpoint called");
        return ResponseEntity.ok(Map.of("message", "Hello from test controller", "status", "working"));
    }

    @GetMapping("/dashboard-test")
    public ResponseEntity<?> dashboardTest() {
        logger.info("Dashboard test endpoint called");
        return ResponseEntity.ok(Map.of(
            "message", "Dashboard routing test", 
            "status", "working",
            "controller", "TestController"
        ));
    }
}