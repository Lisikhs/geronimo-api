package com.geronimo.config;

import com.geronimo.model.User;
import com.geronimo.service.IUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class Bootstrap {

    private static Logger LOG = LoggerFactory.getLogger(Bootstrap.class);

    @Autowired
    private IUserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostConstruct
    public void init() {
        User user = userService.getUserByUsername("root");
        if (user == null) {
            LOG.info("Creating root user since it's not in the database yet");
            user = new User("root", passwordEncoder.encode("root"));

            userService.saveOrUpdateUser(user);
        }
    }
}
