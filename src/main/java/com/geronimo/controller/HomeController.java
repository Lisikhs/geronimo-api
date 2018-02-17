package com.geronimo.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class HomeController {

    @GetMapping("/")
    public String publicEndpoint() {
        return "Public for all";
    }

    @GetMapping("/user")
    public String securedEndpoint() {
        return "Only for authorized users";
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @GetMapping("/admin")
    public String adminRoleSecuredEndpoint() {
        return "Only for authorized admins";
    }
}
