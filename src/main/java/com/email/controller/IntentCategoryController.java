package com.email.controller;

import com.email.exception.ApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.email.request.IntentCategoryRequest;
import com.email.service.IntentCategoryService;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
public class IntentCategoryController {

    private static final Logger logger = LoggerFactory.getLogger(IntentCategoryController.class);

	@Autowired
    private IntentCategoryService categoryService;

    @PostMapping("/create/category")
    public ResponseEntity<?> createCategory(@RequestBody IntentCategoryRequest req) {
        logger.info("Creating new intent category: {}", req.getCategoryName());
        try {
            var result = categoryService.createCategory(req);
            logger.info("Intent category created successfully: {}", req.getCategoryName());
            return ResponseEntity.ok(new ApiResponse("1", result));
        } catch (Exception e) {
            logger.error("Error creating intent category: {}", req.getCategoryName(), e);
            throw e;
        }
    }

    @GetMapping("/fetch/category/all")
    public ResponseEntity<?> getAllCategories() {
        logger.info("Fetching all intent categories");
        try {
            var categories = categoryService.getAllCategories();
            logger.info("Successfully fetched {} intent categories", categories.size());
            return ResponseEntity.ok(new ApiResponse("1", categories));
        } catch (Exception e) {
            logger.error("Error fetching all intent categories", e);
            throw e;
        }
    }
}
