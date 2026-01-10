package com.email.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.email.constants.EmailStatus;
import com.email.entity.EmailLog;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
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

    // Statistics queries
    @Query("SELECT COUNT(e) FROM EmailLog e WHERE e.person.id = :personId")
    Long countTotalEmailsByPersonId(@Param("personId") Integer personId);

    @Query("SELECT COUNT(e) FROM EmailLog e WHERE e.person.id = :personId AND e.sentAt >= :startDate AND e.sentAt < :endDate")
    Long countEmailsByPersonIdAndDateRange(@Param("personId") Integer personId, 
                                          @Param("startDate") LocalDateTime startDate, 
                                          @Param("endDate") LocalDateTime endDate);

    @Query("SELECT COUNT(e) FROM EmailLog e WHERE e.person.id = :personId AND e.status IN :responseStatuses")
    Long countResponsesByPersonId(@Param("personId") Integer personId, @Param("responseStatuses") List<EmailStatus> responseStatuses);

    @Query("SELECT e.status, COUNT(e) FROM EmailLog e WHERE e.person.id = :personId GROUP BY e.status")
    List<Object[]> getStatusBreakdownByPersonId(@Param("personId") Integer personId);

    @Query("SELECT COUNT(DISTINCT e.company) FROM EmailLog e WHERE e.person.id = :personId AND e.company IS NOT NULL")
    Long countUniqueCompaniesByPersonId(@Param("personId") Integer personId);

    @Query("SELECT COUNT(DISTINCT DATE(e.sentAt)) FROM EmailLog e WHERE e.person.id = :personId AND e.sentAt >= :startDate")
    Long countActiveCampaignDaysByPersonId(@Param("personId") Integer personId, @Param("startDate") LocalDateTime startDate);

    // Previous period comparisons
    @Query("SELECT COUNT(e) FROM EmailLog e WHERE e.person.id = :personId AND e.sentAt >= :previousStartDate AND e.sentAt < :previousEndDate")
    Long countEmailsByPersonIdAndPreviousPeriod(@Param("personId") Integer personId, 
                                               @Param("previousStartDate") LocalDateTime previousStartDate, 
                                               @Param("previousEndDate") LocalDateTime previousEndDate);

    @Query("SELECT COUNT(e) FROM EmailLog e WHERE e.person.id = :personId AND e.status IN :responseStatuses AND e.sentAt >= :previousStartDate AND e.sentAt < :previousEndDate")
    Long countResponsesByPersonIdAndPreviousPeriod(@Param("personId") Integer personId, 
                                                  @Param("responseStatuses") List<EmailStatus> responseStatuses,
                                                  @Param("previousStartDate") LocalDateTime previousStartDate, 
                                                  @Param("previousEndDate") LocalDateTime previousEndDate);
}
