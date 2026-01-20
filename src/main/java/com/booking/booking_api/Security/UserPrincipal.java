package com.booking.booking_api.Security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import com.booking.booking_api.Enity.Role;

import java.util.Collection;

public class UserPrincipal implements Authentication {

    private final String email;
    private final String role;
    private boolean authenticated = true;

    public UserPrincipal(String email, String role2) {
        this.email = email;
        this.role = role2;
    }

    @Override public Collection<? extends GrantedAuthority> getAuthorities() { return null; }
    @Override public Object getCredentials() { return null; }
    @Override public Object getDetails() { return null; }
    @Override public Object getPrincipal() { return email; }
    @Override public boolean isAuthenticated() { return authenticated; }
    @Override public void setAuthenticated(boolean isAuthenticated) { this.authenticated = isAuthenticated; }
    @Override public String getName() { return email; }

    public String getRole() { return role; }
}