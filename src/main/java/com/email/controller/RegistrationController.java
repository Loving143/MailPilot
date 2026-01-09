package com.email.controller;

import com.email.exception.ApiResponse;
import com.email.request.SendEmailOtpReq;
import com.email.request.VerifyOtpRequest;
import com.email.service.RegistrationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(originPatterns = "*", maxAge = 3600, allowCredentials = "true")
public class RegistrationController {

    private static final Logger logger = LoggerFactory.getLogger(RegistrationController.class);

    @Autowired
    private RegistrationService service;

    @PostMapping("send-otp")
    public ResponseEntity<?> sendOtp(@RequestBody SendEmailOtpReq req){
        logger.info("OTP send request for email: {}", req.getEmail());
        try {
            service.sendOtp(req);
            logger.info("OTP sent successfully to: {}", req.getEmail());
            return ResponseEntity.ok(new ApiResponse("1","Otp sent successfully!!"));
        } catch (Exception e) {
            logger.error("Error sending OTP to: {}", req.getEmail(), e);
            throw e;
        }
    }

    @PostMapping("verify-otp")
    public ResponseEntity<?> verifyOtp(@RequestBody VerifyOtpRequest req){
        logger.info("OTP verification request for email: {}", req.getEmail());
        try {
            service.verifyOtp(req);
            logger.info("OTP verified successfully for: {}", req.getEmail());
            return ResponseEntity.ok(new ApiResponse("1","Otp verified successfully!!"));
        } catch (Exception e) {
            logger.error("Error verifying OTP for: {}", req.getEmail(), e);
            throw e;
        }
    }
}
