package com.email.controller;

import com.email.exception.ApiResponse;
import com.email.request.SendEmailOtpReq;
import com.email.request.VerifyOtpRequest;
import com.email.service.RegistrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RegistrationController {

    @Autowired
    private RegistrationService service;

    @PostMapping("send-otp")
    public ResponseEntity<?> sendOtp(@RequestBody SendEmailOtpReq req){
        service.sendOtp(req);
        return ResponseEntity.ok(new ApiResponse("1","Otp sent successfully!!"));
    }

    @PostMapping("verify-otp")
    public ResponseEntity<?> verifyOtp(@RequestBody VerifyOtpRequest req){
        service.verifyOtp(req);
        return ResponseEntity.ok(new ApiResponse("1","Otp verified successfully!!"));
    }


}
