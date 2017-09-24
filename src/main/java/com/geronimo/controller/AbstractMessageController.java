package com.geronimo.controller;


import com.geronimo.controller.resource.MessageResource;
import com.geronimo.model.Message;
import com.geronimo.service.IMessageService;
import com.geronimo.service.IUserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class AbstractMessageController {

    private ModelMapper modelMapper;
    private IUserService userService;
    private IMessageService messageService;

    public Message convertToEntity(MessageResource dto) {
        return modelMapper.map(dto, Message.class);
    }

    public MessageResource convertToDto(Message entity) {
        return modelMapper.map(entity, MessageResource.class);
    }

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

    public ModelMapper getModelMapper() {
        return modelMapper;
    }

    @Autowired
    public void setModelMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }
}
