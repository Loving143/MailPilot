package com.email.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.email.request.IntentCategoryRequest;
import com.email.service.IntentCategoryService;

@RestController
public class IntentCategoryController {

	@Autowired
    private IntentCategoryService categoryService;

    @PostMapping("/create/category")
    public ResponseEntity<?> createCategory(@RequestBody IntentCategoryRequest req) {
        return ResponseEntity.ok(categoryService.createCategory(req));
    }

    @GetMapping("/fetch/category/all")
    public ResponseEntity<?> getAllCategories() {
        return ResponseEntity.ok(categoryService.getAllCategories());
    }
}
