package com.email.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.email.master.IntentCategory;
import com.email.repository.IntentCategoryRepository;
import com.email.request.IntentCategoryRequest;
import com.email.resposne.IntentCategoryResponse;

@Service
public class IntentCategoryService {
	
	 @Autowired
	 private IntentCategoryRepository categoryRepo;

	 public IntentCategory createCategory(IntentCategoryRequest req) {

	        if (categoryRepo.findByCategoryCode(req.getCategoryCode()).isPresent()) {
	            throw new RuntimeException("Category already exists");
	        }

	        IntentCategory category = new IntentCategory();
	        category.setCategoryCode(req.getCategoryCode());
	        category.setName(req.getName());
	        category.setDescription(req.getDescription());

	        return categoryRepo.save(category);
	     }

		public List<IntentCategoryResponse> getAllCategories() {
			return categoryRepo.findAll().stream().
					map(IntentCategoryResponse::new).collect(Collectors.toList());
		}
}
