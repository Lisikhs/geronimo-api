package com.geronimo.controller;

import com.geronimo.controller.resource.MessageResource;
import com.geronimo.controller.resource.PageResource;
import com.geronimo.model.Message;
import com.geronimo.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/user/{userId}/message")
public class MessageController extends AbstractMessageController {

    @PostMapping
    public void postMessage(@PathVariable("userId") Long userId, @RequestBody MessageResource messageDto,
                                           HttpServletResponse response) {
        Message message = convertToEntity(messageDto);

        User author = getUserService().getUserById(userId);
        message.setAuthor(author);

        getMessageService().postMessage(message);

        response.setStatus(HttpStatus.CREATED.value());
    }

    @GetMapping
    public PageResource<MessageResource> getMessages(@PathVariable("userId") Long userId, Pageable pageable) {
        // get the user
        User author = getUserService().getUserById(userId);

        // read user messages and reblogs
        Page<Message> messages = getMessageService().listUserMessagesAndReblogs(author, pageable);

        // convert to renderable object
        List<MessageResource> messageDtoList = messages.getContent().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());

        // create a renderable page
        PageImpl<MessageResource> page = new PageImpl<>(messageDtoList, pageable, messages.getTotalElements());

        // get user messages and reblogs pageable)
        return new PageResource<>(page);
    }

}
