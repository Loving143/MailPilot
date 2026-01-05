package com.email.controller;

import com.email.exception.ApiResponse;
import com.email.request.ProfileUpdateRequest;
import com.email.resposne.UserProfileResponse;
import com.email.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private PersonService service;
//    @PreAuthorize("hasRole('USER')")

    @PreAuthorize("hasRole('USER')")
    @PutMapping("/logout")
    public ResponseEntity<?> logOutUser(@RequestHeader("Authorization") String authHeader){
        service.logOutUser(authHeader);
        return ResponseEntity.ok(new ApiResponse("1","User logged out successfully"));
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/profile")
    public ResponseEntity<?> fetchCurrentUserProfile(){
        return  ResponseEntity.ok(new ApiResponse("1", service.fetchCurrentUserProfile()));
    }

    @PreAuthorize("hasRole('USER')")
    @PutMapping("/profile/update")
    public ResponseEntity<?> updateCurrentProfile(@RequestBody ProfileUpdateRequest req){
        service.updateProfile(req);
        return ResponseEntity.ok(new ApiResponse("1","Profile updated successfully!"));
    }


}
