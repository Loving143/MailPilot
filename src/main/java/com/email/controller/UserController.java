package com.email.controller;

import com.email.request.ProfileUpdateRequest;
import com.email.resposne.UserProfileResponse;
import com.email.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
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

    @PutMapping("/logout")
    public String logOutUser(@RequestHeader("Authorization") String authHeader){
        service.logOutUser(authHeader);
        return "User logged out successfully";
    }

    @GetMapping("/profile")
    public UserProfileResponse fetchCurrentUserProfile(){
        return service.fetchCurrentUserProfile();
    }

    @PutMapping("/profile/update")
    public String updateCurrentProfile(@RequestBody ProfileUpdateRequest req){
        service.updateProfile(req);
        return "profile updated successfully!";
    }


}
