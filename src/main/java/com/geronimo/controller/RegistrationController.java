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
@RequestMapping("/register")
@CrossOrigin(origins = "http://localhost:4200")
public class RegistrationController {
    private static final Logger logger = LoggerFactory.getLogger(RegistrationController.class);
    private final UserService userService;

    @Autowired
    RegistrationController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public User register(@RequestBody String data) {
        try {
            JsonReader jsonReader = new JsonReader(data);
            String username = jsonReader.readText("username");
            String password = jsonReader.readText("password");

            User user = new User(username, password);
            return userService.saveOrUpdateUser(user);
        } catch (IOException e) {
            logger.error("Failed to read JSON data", e);
        }
        throw new IllegalStateException("Failed to save user");
    }
}
