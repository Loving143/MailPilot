package com.email.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.email.entity.Otp;
import com.email.entity.Person;
import org.springframework.data.repository.query.Param;

public interface OtpRepository extends JpaRepository<Otp,Integer>{
	
	Optional<Otp> findByOtpAndUsedFalseOrderByCreatedAtDesc(String email);

	@Query("Select "
			+ " otpp from Otp otpp "
			+ " join otpp.person as person "
			+ " where person.email =:email"
			+ " AND "
			+ " otpp.used = false "
			+ " AND "
			+ " otpp.otp =:otp ")
	Optional<Otp> findValidOtp( @Param("otp") String otp,
								@Param("email") String email);

	@Query("" +
			"Select otpp " +
			"from Otp otpp " +
			" inner join otpp.person person " +
			" where person.email =:email" +
			" AND otpp.used = false"
	 )
	Optional<Otp> findOtpByEmail(String email);
}
