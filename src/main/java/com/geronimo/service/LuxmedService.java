package com.geronimo.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;


@Service
public class LuxmedService implements ILuxmedService {

    private RestTemplate luxmedRestTemplate;

    @Override
    public String loginIntoLuxmed(String username, char[] password) {
        // TODO: do a POST request to Luxmed Login endpoint
        // and return a secret token value from Set-Cookie header

        return "fake_token";
    }

    @Autowired
    public void setLuxmedRestTemplate(RestTemplate luxmedRestTemplate) {
        this.luxmedRestTemplate = luxmedRestTemplate;
    }
}
