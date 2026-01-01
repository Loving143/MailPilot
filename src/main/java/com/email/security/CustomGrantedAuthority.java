package com.email.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

@Component
public class CustomGrantedAuthority implements GrantedAuthority {

    @Override
    public String getAuthority() {
        return "";
    }
}
