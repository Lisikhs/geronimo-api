package com.geronimo.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class HomeController {

    @GetMapping("/")
    public String index() {
        return "Hello world";
    }

    @PreAuthorize("hasAuthority('ADMIN_HOME')")
    @GetMapping("/admin")
    public String adminIndex() {
        return "Restricted only for admins";
    }
}
