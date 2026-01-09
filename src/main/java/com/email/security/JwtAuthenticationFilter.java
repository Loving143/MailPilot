package com.email.security;
import java.io.IOException;
import java.util.List;

import com.email.exception.BadRequestException;
import com.email.repository.UserSessionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    @Autowired
    private JwtUtil jwtTokenUtil;

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Autowired
    private UserSessionRepository userSessionRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        final String authorizationHeader = request.getHeader("Authorization");
        String requestURI = request.getRequestURI();
        String method = request.getMethod();
        
        logger.debug("Processing request: {} {}", method, requestURI);

        String username = null;
        String jwt = null;

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            jwt = authorizationHeader.substring(7);
            try {
                username = jwtTokenUtil.extractUsername(jwt);
                logger.debug("Extracted username from JWT: {}", username);
            } catch (Exception e) {
                logger.warn("Failed to extract username from JWT token", e);
            }
        } else {
            logger.debug("No Authorization header found or invalid format for request: {} {}", method, requestURI);
        }

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            logger.debug("Attempting to authenticate user: {}", username);
            try {
                UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);

                if (jwtTokenUtil.validateToken(jwt, userDetails)) {
                    logger.debug("JWT token validated successfully for user: {}", username);

                    boolean isSessionActive =
                            userSessionRepository.findByAccessTokenAndActiveTrue(jwt).isPresent();
                    if(!isSessionActive){
                        logger.warn("Session is inactive or user is logged out for user: {}", username);
                        throw new BadRequestException("User is logged out or session is inactive!");
                    }
                    
                    List<String> roles = jwtTokenUtil.extractRoles(jwt);
                    logger.debug("Extracted roles for user {}: {}", username, roles);
                    
                    List<CustomGrantedAuthority> authorities =
                            roles.stream()
                                    .map(CustomGrantedAuthority::new)
                                    .toList();
                    UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                            new UsernamePasswordAuthenticationToken(userDetails, null, authorities);
                    SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
                    
                    logger.info("User authenticated successfully: {} with roles: {}", username, roles);
                } else {
                    logger.warn("JWT token validation failed for user: {}", username);
                }
            } catch (Exception e) {
                logger.error("Authentication failed for user: {}", username, e);
                throw e;
            }
        }

        filterChain.doFilter(request, response);
    }
}