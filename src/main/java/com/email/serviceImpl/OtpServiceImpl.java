package com.email.serviceImpl;

import com.email.entity.Otp;
import com.email.exception.BadRequestException;
import com.email.repository.OtpRepository;
import com.email.service.OtpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OtpServiceImpl implements OtpService {

    @Autowired
    private OtpRepository otpRepository;

    @Override
    public Otp findOtpByEmail(String email) {
        return otpRepository.findOtpByEmail(email).orElseThrow(()-> new BadRequestException("Otp with this email does not exists!"));
    }
}
