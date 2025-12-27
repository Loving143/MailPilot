package com.email.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.email.master.IntentCategory;

public interface IntentCategoryRepository extends JpaRepository<IntentCategory, Integer>{
	
	Optional<IntentCategory> findByCategoryCode(String categoryCode);
}
