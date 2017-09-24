package com.geronimo.controller;

import com.geronimo.controller.resource.MessageResource;
import com.geronimo.controller.resource.PageResource;
import com.geronimo.controller.resource.converter.MessageConverter;
import com.geronimo.model.Message;
import com.geronimo.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/user/{userId}/feed")
public class MessageFeedController extends AbstractMessageController {

    @Autowired
    private MessageConverter messageConverter;

    @GetMapping
    public PageResource<MessageResource> listFeedMessages(@PathVariable("userId") Long userId, Pageable pageable) {
        // get the user
        User author = getUserService().getUserById(userId);

        // get user messages and reblogs
        Page<Message> feedMessages = getMessageService().listFeedMessages(author, pageable);

        // TODO: can we somehow generalize this?
        // convert to renderable object
        List<MessageResource> messageDtoList = feedMessages.getContent().stream()
                .map(messageConverter::convert)
                .collect(Collectors.toList());

        // create a renderable page
        PageImpl<MessageResource> page = new PageImpl<>(messageDtoList, pageable, feedMessages.getTotalElements());

        return new PageResource<>(page);
    }
}
