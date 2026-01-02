package com.email.repository;

import com.email.entity.UserSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.Optional;

public interface UserSessionRepository extends JpaRepository<UserSession,Integer> {

    @Modifying
    @Query("""
        update UserSession s
        set s.isActive = false, s.logoutTime = :logoutTime
        where s.jwtToken = :token and s.isActive = true
    """)
    int invalidateSession(@Param("token") String token,
                          @Param("logoutTime") LocalDateTime logoutTime);


    @Query("""
    SELECT s
    FROM UserSession s
    WHERE s.isActive = true
      AND s.jwtToken = :jwt
""")
    Optional<UserSession> findByAccessTokenAndActiveTrue(@Param("jwt") String jwt);
}
