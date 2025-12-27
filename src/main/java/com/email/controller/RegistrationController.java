package com.email.controller;

import com.email.request.SendEmailOtpReq;
import com.email.request.VerifyOtpRequest;
import com.email.service.RegistrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RegistrationController {

    @Autowired
    private RegistrationService service;

    @PostMapping("send-otp")
    public String sendOtp(@RequestBody SendEmailOtpReq req){
        service.sendOtp(req);
        return "Otp sent successfully!!";
    }

    @PostMapping("verify-otp")
    public String verifyOtp(@RequestBody VerifyOtpRequest req){
        service.verifyOtp(req);
        return "Otp verified successfully!!";
    }


}
