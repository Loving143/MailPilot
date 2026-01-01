package com.email.service;

import com.email.request.SendEmailOtpReq;
import com.email.request.VerifyOtpRequest;

public interface RegistrationService {
    void sendOtp(SendEmailOtpReq req);

    void verifyOtp(VerifyOtpRequest req);

    boolean verifyOtp(String username, String otp);
}
