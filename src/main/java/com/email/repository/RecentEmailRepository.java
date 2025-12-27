package com.email.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.email.entity.RecentEmail;

public interface RecentEmailRepository extends JpaRepository<RecentEmail,Integer>{

	Optional<RecentEmail> findByPersonId(Integer id);
	
}
