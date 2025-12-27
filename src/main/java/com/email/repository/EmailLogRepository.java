package com.email.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.email.entity.EmailLog;

import java.util.Optional;

public interface EmailLogRepository extends JpaRepository<EmailLog, Long>{

    boolean existsByRecipientEmail(String recipientEmail);

    Optional<EmailLog> findByRecipientEmail(String recipientEmail);
}
