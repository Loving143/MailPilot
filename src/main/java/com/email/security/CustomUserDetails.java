package com.email.security;

import com.email.entity.Person;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class CustomUserDetails implements UserDetails{

    private final Person person;

    public CustomUserDetails(Person person) {
        this.person = person;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return person.getRoles().stream().
                map(role->new CustomGrantedAuthority(role.getRoleCode()))
                .collect(Collectors.toList());
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public String getUsername() {
        return person.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return person.isAccountNonExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return person.isAccountNonLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return person.isCredentialsNonExpired();
    }

    @Override
    public boolean isEnabled() {
        return person.isEnabled();
    }
}
