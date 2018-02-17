package com.geronimo.controller;

import com.geronimo.model.Message;
import com.geronimo.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/user/{userId}/feed")
public class MessageFeedController extends AbstractMessageController {

    @GetMapping
    public ResponseEntity<Page<Message>> listFeedMessages(@PathVariable("userId") Long userId, Pageable pageable) {
        User author = getUserService().getUserById(userId);
        Page<Message> feedMessages = getMessageService().listFeedMessages(author, pageable);

        return new ResponseEntity<>(feedMessages, HttpStatus.OK);
    }
}
