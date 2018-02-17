package com.geronimo.config.security;

import com.geronimo.model.Role;
import com.geronimo.model.User;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.stream.Collectors;

public class UserDetailsFactory {

    public static UserDetails fromUser(User user) {
        return new UserDetails(user.getUsername(),
                user.getPassword(),
                user.isAccountNonExpired(),
                user.isAccountNonLocked(),
                user.isCredentialsNonExpired(),
                user.isEnabled(),
                user.getLastPasswordReset(),
                user.getRoles().parallelStream()
                        .map(Role::getName)
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList()));
    }
}
