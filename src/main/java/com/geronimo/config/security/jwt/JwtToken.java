package com.geronimo.config.security.jwt;

public class JwtToken {
    private final String token;

    public JwtToken(String token) {
        this.token = token;
    }

    public String getToken() {
        return this.token;
    }
}
