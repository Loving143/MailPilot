package com.email.service;

import com.email.entity.Otp;

public interface OtpService {
    Otp findOtpByEmail(String email);
}
