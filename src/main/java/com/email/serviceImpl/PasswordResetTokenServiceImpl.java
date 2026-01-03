package com.email.serviceImpl;

import com.email.entity.PasswordResetToken;
import com.email.entity.Person;
import com.email.repository.PasswordResetTokenRepository;
import com.email.repository.PersonRepository;
import com.email.service.EmailService;
import com.email.service.PasswordResetTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class PasswordResetTokenServiceImpl implements PasswordResetTokenService {
    @Autowired
    private PasswordResetTokenRepository tokenRepository;

    @Autowired
    private PersonRepository userRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public void sendPasswordResetToken(String email) {
        Person user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        String token = UUID.randomUUID().toString();
        PasswordResetToken resetToken = new PasswordResetToken(token, user);
        tokenRepository.save(resetToken);

        String resetUrl = "http://localhost:8080/api/auth/reset-password?token=" + token;
        emailService.sendPasswordResetEmail(user.getEmail(),
                "Click the link to reset your password: " + resetUrl, "Password Reset Request");
    }

    @Override
    public void updatePassword(String token, String newPassword) {
        PasswordResetToken resetToken = tokenRepository.findByToken(token)
                .orElseThrow(() -> new RuntimeException("Invalid token"));

        if (resetToken.isExpired()) {
            throw new RuntimeException("Token has expired");
        }

        Person user = resetToken.getUser();
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        tokenRepository.delete(resetToken);
    }
}
