package com.email.serviceImpl;

import com.email.repository.UserSessionRepository;
import com.email.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class PersonServiceImpl implements PersonService {

    @Autowired
    private UserSessionRepository sessionRepository;
    @Override
    public void logOutUser(String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new RuntimeException("Invalid Authorization header");
        }

        String token = authHeader.substring(7);

        int updated = sessionRepository.invalidateSession(
                token, LocalDateTime.now());

        if (updated == 0) {
            throw new RuntimeException("Session already invalid or not found");
        }
    }
}
