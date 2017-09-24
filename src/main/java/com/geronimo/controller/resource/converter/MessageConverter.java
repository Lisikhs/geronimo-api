package com.geronimo.controller.resource.converter;


import com.geronimo.controller.MessageController;
import com.geronimo.controller.resource.MessageResource;
import com.geronimo.model.Message;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.*;


@Component
public class MessageConverter implements ResourceConverter<Message, MessageResource> {

    private ModelMapper modelMapper;

    @Override
    public MessageResource convert(Message entity) {
        MessageResource resource = modelMapper.map(entity, MessageResource.class);

        resource.add(linkTo(MessageController.class, entity.getAuthor().getId())
                .slash(entity.getId())
                .withRel(Link.REL_SELF));

        return resource;
    }

    @Override
    public Message convert(MessageResource dto) {
        return modelMapper.map(dto, Message.class);
    }

    @Override
    public ModelMapper getModelMapper() {
        return modelMapper;
    }

    @Autowired
    @Override
    public void setModelMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }
}
