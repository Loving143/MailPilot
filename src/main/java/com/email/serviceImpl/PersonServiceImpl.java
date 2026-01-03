package com.email.serviceImpl;

import com.email.entity.Person;
import com.email.repository.PersonRepository;
import com.email.repository.UserSessionRepository;
import com.email.request.ProfileUpdateRequest;
import com.email.resposne.UserProfileResponse;
import com.email.service.PersonService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class PersonServiceImpl implements PersonService {

    private final PersonRepository personRepository;

    public PersonServiceImpl(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }
    @Autowired
    private UserSessionRepository sessionRepository;
    @Override
    @Transactional
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

    @Override
    public UserProfileResponse fetchCurrentUserProfile() {
      String currentUsername = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            Person person = personRepository.findByEmail(currentUsername)
                    .orElseThrow(() -> new UsernameNotFoundException("User not found"));
            return new UserProfileResponse(person);
    }

    @Override
    public void updateProfile(ProfileUpdateRequest req) {
        String currentUsername = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Person person = personRepository.findByEmail(currentUsername)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        // Update profile logic here
        person.setNamee(req.getName());
        person.setMobNo(req.getMobNo());
        personRepository.save(person);

    }


}
