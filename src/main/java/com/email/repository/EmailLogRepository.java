package com.email.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.email.entity.EmailLog;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface EmailLogRepository extends JpaRepository<EmailLog, Long>{

    boolean existsByRecipientEmail(String recipientEmail);

    Optional<EmailLog> findByRecipientEmail(String recipientEmail);

    @Query("SELECT e " +
            " FROM EmailLog e " +
            " inner join e.person p " +
            " WHERE p.id = :id ")
    List<EmailLog> findByPersonId(Integer id);
}
