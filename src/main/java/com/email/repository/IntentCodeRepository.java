package com.email.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.email.master.IntentCode;

public interface IntentCodeRepository extends JpaRepository<IntentCode,Integer>{

	 Optional<IntentCode> findByCode(String code);

	 List<IntentCode> findByCategory_CategoryCode(String categoryCode);

}
