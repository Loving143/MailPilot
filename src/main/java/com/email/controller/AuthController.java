package com.email.controller;

import com.email.entity.Otp;
import com.email.request.SendEmailOtpReq;
import com.email.request.VerifyOtpRequest;
import com.email.resposne.LoginResponse;
import com.email.security.JwtUtil;
import com.email.service.OtpService;
import com.email.service.RegistrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    OtpService otpService;	

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private RegistrationService service;

    @PostMapping("/verify-otp")
    public LoginResponse VerifyOtp(@RequestBody VerifyOtpRequest otpReq){
    	System.out.println(otpReq.getEmail());
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(otpReq.getEmail(), otpReq.getOtp()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        Otp otp =otpService.findOtpByEmail(otpReq.getEmail());
        otp.setUsed(true);
        String jwtToken = jwtUtil.generateToken(otpReq.getEmail());
//	        return "JWT Token: " + jwtToken;
        LoginResponse response = new LoginResponse(jwtToken);
        //System.out.println(response.getEmail());
        return response;
    }

    @PostMapping("send-otp")
    public String sendOtp(@RequestBody SendEmailOtpReq req){
        service.sendOtp(req);
        return "Otp sent successfully!!";
    }
    
 // create a REST API to fetch user by id
    	

}
