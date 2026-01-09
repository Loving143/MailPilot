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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
@CrossOrigin(originPatterns = "*", maxAge = 3600, allowCredentials = "true")
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

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
        logger.info("OTP verification attempt for email: {}", otpReq.getEmail());
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(otpReq.getEmail(), otpReq.getOtp()));
            SecurityContextHolder.getContext().setAuthentication(authentication);
            
            Otp otp = otpService.findOtpByEmail(otpReq.getEmail());
            otp.setUsed(true);
            Person person = otp.getPerson();
            otpRepository.save(otp);
            
            String jwtToken = jwtUtil.generateToken(authentication, otpReq.getEmail());
            UserSession session = new UserSession(jwtToken, person);
            userSessionRepository.save(session);
            
            logger.info("OTP verification successful for email: {}", otpReq.getEmail());
            LoginResponse response = new LoginResponse(jwtToken);
            return ResponseEntity.ok(new ApiResponse("1", response));
        } catch (Exception e) {
            logger.error("OTP verification failed for email: {}", otpReq.getEmail(), e);
            throw e;
        }
    }

    @PostMapping("send-otp")
    public ResponseEntity<?> sendOtp(@RequestBody SendEmailOtpReq req){
        logger.info("OTP send request for email: {}", req.getEmail());
        try {
            service.sendOtp(req);
            logger.info("OTP sent successfully to email: {}", req.getEmail());
            return ResponseEntity.ok(new ApiResponse<>("1","Otp sent successfully!!"));
        } catch (Exception e) {
            logger.error("Failed to send OTP to email: {}", req.getEmail(), e);
            throw e;
        }
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestParam String email) {
        logger.info("Password reset request for email: {}", email);
        try {
            passwordResetService.sendPasswordResetToken(email);
            logger.info("Password reset link sent to email: {}", email);
            return ResponseEntity.ok(new ApiResponse<>("1","Password reset link sent to email"));
        } catch (Exception e) {
            logger.error("Failed to send password reset link to email: {}", email, e);
            throw e;
        }
    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestParam String token, @RequestParam String newPassword) {
        logger.info("Password reset attempt with token");
        try {
            passwordResetService.updatePassword(token, newPassword);
            logger.info("Password reset successful");
            return ResponseEntity.ok(new ApiResponse<>("1","Password has been reset successfully"));
        } catch (Exception e) {
            logger.error("Password reset failed", e);
            throw e;
        }
    }
}
