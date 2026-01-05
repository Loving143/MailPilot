package com.email.controller;

import com.email.entity.Otp;
import com.email.entity.Person;
import com.email.entity.UserSession;
import com.email.exception.ApiResponse;
import com.email.repository.OtpRepository;
import com.email.repository.UserSessionRepository;
import com.email.request.SendEmailOtpReq;
import com.email.request.VerifyOtpRequest;
import com.email.resposne.LoginResponse;
import com.email.security.JwtUtil;
import com.email.service.OtpService;
import com.email.service.PasswordResetTokenService;
import com.email.service.RegistrationService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

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

    @Autowired
    private PasswordResetTokenService passwordResetService;

    @PostMapping("/verify-otp")
    public ResponseEntity<?> VerifyOtp(@RequestBody VerifyOtpRequest otpReq){
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(otpReq.getEmail(), otpReq.getOtp()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        Otp otp =otpService.findOtpByEmail(otpReq.getEmail());
        otp.setUsed(true);
        Person person = otp.getPerson();
        otpRepository.save(otp);
        String jwtToken = jwtUtil.generateToken(authentication,otpReq.getEmail());
        UserSession session = new UserSession(jwtToken,person);
        userSessionRepository.save(session);
        LoginResponse response = new LoginResponse(jwtToken);
        return ResponseEntity.ok(new ApiResponse("1",response));
    }

    @PostMapping("send-otp")
    public ResponseEntity<?> sendOtp(@RequestBody SendEmailOtpReq req){
        service.sendOtp(req);
        return ResponseEntity.ok(new ApiResponse<>("1","Otp sent successfully!!"));
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestParam String email) {
        passwordResetService.sendPasswordResetToken(email);
        return ResponseEntity.ok(new ApiResponse<>("1","Password reset link sent to email"));
    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestParam String token, @RequestParam String newPassword) {
        passwordResetService.updatePassword(token, newPassword);
        return ResponseEntity.ok(new ApiResponse<>("1","Password has been reset successfully"));
    }
    	

}
