package com.email.controller;

import com.email.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    @Autowired
    private PersonService service;
    @PreAuthorize("hasRole('USER')")
    public String logOutUser(@RequestHeader("Authorization") String authHeader){
        service.logOutUser(authHeader);
        return "User logged out successfully";
    }
}
