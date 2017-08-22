package com.geronimo.controller;

import com.geronimo.service.ILuxmedService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LuxmedLoginController {

    private Logger logger = LoggerFactory.getLogger(LuxmedLoginController.class);

    private ILuxmedService luxmedService;

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String loginIntoLuxmed(@RequestParam() String username, @RequestParam char[] password) {
        logger.info("Trying to login with credentials: {}, {}", username, password);
        String token = luxmedService.loginIntoLuxmed(username, password);

        return token;
    }

    @Autowired
    public void setLuxmedService(ILuxmedService luxmedService) {
        this.luxmedService = luxmedService;
    }
}
