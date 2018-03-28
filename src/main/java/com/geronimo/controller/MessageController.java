package com.geronimo.controller;

import com.geronimo.model.Message;
import com.geronimo.model.User;
import com.geronimo.service.IMessageService;
import com.geronimo.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/user/{userId}/message")
public class MessageController {

    private IUserService userService;
    private IMessageService messageService;

    @Autowired
    public MessageController(IMessageService messageService, IUserService userService) {
        this.messageService = messageService;
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity postMessage(@PathVariable("userId") Long userId, @RequestBody Message message) {
        User author = userService.getById(userId);
        message.setAuthor(author);

        messageService.postMessage(message);

        return new ResponseEntity(HttpStatus.CREATED);
    }

    @GetMapping("/{msgId}")
    public ResponseEntity<Message> getMessage(@PathVariable("msgId") Long msgId) {
        Message message = messageService.getMessageById(msgId);

        if (message == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<Page<Message>> getMessages(@PathVariable("userId") Long userId,
                                                     @RequestParam("feed") Boolean feed, Pageable pageable) {
        User author = userService.getById(userId);

        Page<Message> messages;
        if (feed) {
            messages = messageService.listFeedMessages(author, pageable);
        } else {
            messages = messageService.listUserMessagesAndReblogs(author, pageable);
        }

        return new ResponseEntity<>(messages, HttpStatus.OK);
    }
}
