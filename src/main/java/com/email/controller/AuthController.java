package com.email.controller;

import com.email.entity.Otp;
import com.email.entity.Person;
import com.email.entity.UserSession;
import com.email.repository.OtpRepository;
import com.email.repository.UserSessionRepository;
import com.email.request.SendEmailOtpReq;
import com.email.request.VerifyOtpRequest;
import com.email.resposne.LoginResponse;
import com.email.security.JwtUtil;
import com.email.service.OtpService;
import com.email.service.RegistrationService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
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
    @Autowired
    private OtpRepository otpRepository;
    @Autowired
    private UserSessionRepository userSessionRepository;

    @PostMapping("/verify-otp")
    public LoginResponse VerifyOtp(@RequestBody VerifyOtpRequest otpReq){
    	System.out.println(otpReq.getEmail());
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(otpReq.getEmail(), otpReq.getOtp()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        Otp otp =otpService.findOtpByEmail(otpReq.getEmail());
        otp.setUsed(true);
        Person person = otp.getPerson();
        otpRepository.save(otp);
        String jwtToken = jwtUtil.generateToken(otpReq.getEmail());
        UserSession session = new UserSession(jwtToken,person);
        userSessionRepository.save(session);
        LoginResponse response = new LoginResponse(jwtToken);
        return response;
    }

    @PostMapping("send-otp")
    public String sendOtp(@RequestBody SendEmailOtpReq req){
        service.sendOtp(req);
        return "Otp sent successfully!!";
    }
    
 // create a REST API to fetch user by id
    	

}
