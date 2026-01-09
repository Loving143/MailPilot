package com.email.controller;

import com.email.exception.ApiResponse;
import com.email.request.ProfileUpdateRequest;
import com.email.resposne.UserProfileResponse;
import com.email.service.PersonService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
@CrossOrigin(origins = "*", maxAge = 3600)
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private PersonService service;

    @PreAuthorize("hasRole('USER')")
    @PutMapping("/logout")
    public ResponseEntity<?> logOutUser(@RequestHeader("Authorization") String authHeader){
        logger.info("User logout request received");
        try {
            service.logOutUser(authHeader);
            logger.info("User logged out successfully");
            return ResponseEntity.ok(new ApiResponse("1","User logged out successfully"));
        } catch (Exception e) {
            logger.error("Error during user logout", e);
            throw e;
        }
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/profile")
    public ResponseEntity<?> fetchCurrentUserProfile(){
        logger.info("Fetching current user profile");
        try {
            var profile = service.fetchCurrentUserProfile();
            logger.info("User profile fetched successfully");
            return ResponseEntity.ok(new ApiResponse("1", profile));
        } catch (Exception e) {
            logger.error("Error fetching user profile", e);
            throw e;
        }
    }

    @PreAuthorize("hasRole('USER')")
    @PutMapping("/profile/update")
    public ResponseEntity<?> updateCurrentProfile(@RequestBody ProfileUpdateRequest req){
        logger.info("Profile update request received");
        try {
            service.updateProfile(req);
            logger.info("Profile updated successfully");
            return ResponseEntity.ok(new ApiResponse("1","Profile updated successfully!"));
        } catch (Exception e) {
            logger.error("Error updating profile", e);
            throw e;
        }
    }
}
