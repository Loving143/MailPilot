package com.email.service;

public interface PasswordResetTokenService {
    public void sendPasswordResetToken(String email);

    void updatePassword(String token, String newPassword);
}
