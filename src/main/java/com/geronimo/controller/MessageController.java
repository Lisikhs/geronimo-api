package com.geronimo.controller;

import com.geronimo.model.Message;
import com.geronimo.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;


@RestController
@RequestMapping("/user/{userId}/message")
public class MessageController extends AbstractMessageController {

    @PostMapping
    public void postMessage(@PathVariable("userId") Long userId, @RequestBody Message message,
                            HttpServletResponse response) {
        User author = getUserService().getUserById(userId);
        message.setAuthor(author);

        getMessageService().postMessage(message);

        response.setStatus(HttpStatus.CREATED.value());
    }

    @GetMapping("/{messageId}")
    public ResponseEntity<Message> getMessage(@PathVariable("messageId") Long messageId) {
        Message message = getMessageService().getMessageById(messageId);

        if (message == null) {
            return new ResponseEntity<>(HttpStatus.OK);
        }

        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<Page<Message>> getMessages(@PathVariable("userId") Long userId, Pageable pageable) {
        // get the user
        User author = getUserService().getUserById(userId);

        // read user messages and reblogs
        Page<Message> messages = getMessageService().listUserMessagesAndReblogs(author, pageable);
        return new ResponseEntity<>(messages, HttpStatus.OK);
    }

}
