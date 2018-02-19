package com.geronimo.controller;

import com.geronimo.JsonReader;
import com.geronimo.model.User;
import com.geronimo.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/login")
@CrossOrigin(origins = "http://localhost:4200")
public class LoginController {
    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);
    private final UserService userService;

    @Autowired
    LoginController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public User login(@RequestBody String data) {
        try {
            JsonReader reader = new JsonReader(data);
            String username = reader.readText("username");
            String password = reader.readText("password");
            return userService.getUserByUsernameAndPassword(username, password);
        } catch (IOException e) {
            logger.error("Failed to read JSON data", e);
        }
        throw new IllegalStateException("Failed to authenticate user");
    }
}
