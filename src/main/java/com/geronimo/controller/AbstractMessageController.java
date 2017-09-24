package com.geronimo.controller;


import com.geronimo.service.IMessageService;
import com.geronimo.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;


public abstract class AbstractMessageController {

    private IUserService userService;
    private IMessageService messageService;

    public IUserService getUserService() {
        return userService;
    }

    @Autowired
    public void setUserService(IUserService userService) {
        this.userService = userService;
    }

    public IMessageService getMessageService() {
        return messageService;
    }

    @Autowired
    public void setMessageService(IMessageService messageService) {
        this.messageService = messageService;
    }

}
