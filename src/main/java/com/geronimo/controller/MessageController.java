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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/user/{userId}/message")
public class MessageController extends AbstractMessageController {

    @Autowired
    private MessageConverter messageConverter;

    @PostMapping
    public void postMessage(@PathVariable("userId") Long userId, @RequestBody MessageResource messageDto,
                                           HttpServletResponse response) {
        Message message = messageConverter.convert(messageDto);

        User author = getUserService().getUserById(userId);
        message.setAuthor(author);

        getMessageService().postMessage(message);

        response.setStatus(HttpStatus.CREATED.value());
    }

    @GetMapping("/{messageId}")
    public ResponseEntity<MessageResource> getMessage(@PathVariable("messageId") Long messageId) {
        Message message = getMessageService().getMessageById(messageId);

        if (message == null) {
            return new ResponseEntity<>(HttpStatus.OK);
        }

        MessageResource messageResource = messageConverter.convert(message);
        return new ResponseEntity<>(messageResource, HttpStatus.OK);
    }

    @GetMapping
    public PageResource<MessageResource> getMessages(@PathVariable("userId") Long userId, Pageable pageable) {
        // get the user
        User author = getUserService().getUserById(userId);

        // read user messages and reblogs
        Page<Message> messages = getMessageService().listUserMessagesAndReblogs(author, pageable);

        // convert to renderable object
        List<MessageResource> messageDtoList = messages.getContent().stream()
                .map(messageConverter::convert)
                .collect(Collectors.toList());

        // create a renderable page
        PageImpl<MessageResource> page = new PageImpl<>(messageDtoList, pageable, messages.getTotalElements());

        // get user messages and reblogs pageable)
        return new PageResource<>(page);
    }

}
