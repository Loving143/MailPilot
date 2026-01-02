package com.email.security;


import com.email.service.RegistrationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

public class OtpAuthenticationProvider implements AuthenticationProvider {

    @Autowired
    private RegistrationService registrationService;

    @Autowired
    private CustomUserDetailsService userDetailsService;
    private static final Logger logger = LoggerFactory.getLogger(OtpAuthenticationProvider.class);
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        logger.info("Otp provider executed");

        String username = authentication.getName();
        Object t= authentication.getCredentials();
        String otp = authentication.getCredentials().toString();
        System.out.println(otp +" "+username);
        // ✅ OTP verification yahin ho rahi hai
        if (!registrationService.verifyOtp(username, otp)) {
            throw new RuntimeException("Invalid OTP");
        }
        System.out.println("This is otp");

        UserDetails user = userDetailsService
                .loadUserByUsername(username);

        return new UsernamePasswordAuthenticationToken(
                user, null, user.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
