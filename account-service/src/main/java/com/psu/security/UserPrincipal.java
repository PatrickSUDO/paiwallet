package com.psu.security;

import com.psu.model.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Collection;
import java.util.Collections;

public class UserPrincipal implements UserDetails {
    private final User user;

    public UserPrincipal(User user) {
        this.user = user;
    }

    // Factory method to create a new UserPrincipal instance
    public static UserPrincipal create(User user) {
        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }
        return new UserPrincipal(user);
    }

    // Implement the necessary methods from UserDetails
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Create a new SimpleGrantedAuthority for each role or authority the user has
        // For simplicity, assuming every user has a default role 'USER'
        return Collections.singletonList(new SimpleGrantedAuthority("USER"));
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    // Handle account properties. Returning true for simplicity, but you should implement proper checks
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
