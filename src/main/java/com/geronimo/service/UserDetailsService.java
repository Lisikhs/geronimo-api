package com.geronimo.service;

import com.geronimo.config.security.UserDetails;
import com.geronimo.config.security.UserDetailsFactory;
import com.geronimo.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.transaction.annotation.Transactional;


@Slf4j
public class UserDetailsService implements org.springframework.security.core.userdetails.UserDetailsService {

    @Autowired
    private IUserService userService;

    @Transactional(readOnly = true)
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userService.getByUsername(username);

        if (user == null) {
            throw new UsernameNotFoundException("User with username '" + username + "' was not found!");
        } else {
            return UserDetailsFactory.fromUser(user);
        }
    }
}
