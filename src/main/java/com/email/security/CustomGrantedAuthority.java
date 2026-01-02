package com.email.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

public class CustomGrantedAuthority implements GrantedAuthority {

    private final String authority;

    public CustomGrantedAuthority(String roleCode) {
    this.authority = roleCode;
    }

    @Override
    public String getAuthority() {
       return authority;
    }
}
