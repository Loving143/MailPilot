package com.email.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.email.entity.Otp;
import com.email.entity.Person;

public interface OtpRepository extends JpaRepository<Otp,Integer>{
	
	Optional<Otp> findByOtpAndUsedFalseOrderByCreatedAtDesc(String email);

	@Query("Select "
			+ " otp from Otp otp "
			+ " inner join otp.person as person "
			+ " where person.email =:email"
			+ " AND "
			+ " otp.used = false "
			+ " AND "
			+ " otp.otp =:otp ")
	Optional<Otp> findValidOtp(String otp, String email);

}
