package com.geronimo.service;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.net.InetSocketAddress;


@Service
public class LuxmedService implements ILuxmedService {

    private static final String LOGIN_URL = "https://portalpacjenta.luxmed.pl/PatientPortal/Account/LogIn";
    private static final Logger logger = LoggerFactory.getLogger(LuxmedService.class);

    private RestTemplate luxmedRestTemplate;

    @Override
    public String loginIntoLuxmed(String username, char[] password) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        httpHeaders.setHost(new InetSocketAddress("portalpacjenta.luxmed.pl", 80));
        httpHeaders.setOrigin("https://portalpacjenta.luxmed.pl");
        httpHeaders.add("Referer", "https://portalpacjenta.luxmed.pl/PatientPortal/Account/LogOn");
        httpHeaders.add("User-Agent",
                "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3112.101 Safari/537.36");

        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("username", username);
        formData.add("password", String.valueOf(password));

        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(formData, httpHeaders);

        ResponseEntity<String> responseEntity = luxmedRestTemplate.postForEntity(LOGIN_URL, entity, String.class);

        logger.info(responseEntity.toString());
        return "fake_token";
    }

    @Autowired
    public void setLuxmedRestTemplate(RestTemplate luxmedRestTemplate) {
        this.luxmedRestTemplate = luxmedRestTemplate;
    }
}
