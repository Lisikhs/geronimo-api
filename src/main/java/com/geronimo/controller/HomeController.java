package com.geronimo.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class HomeController {

    @PreAuthorize("permitAll")
    @GetMapping("/")
    public String index() {
        return "Hello world";
    }
}
