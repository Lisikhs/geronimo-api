package com.geronimo.controller;

import com.geronimo.JsonReader;
import com.geronimo.model.Message;
import com.geronimo.model.User;
import com.geronimo.service.MessageService;
import com.geronimo.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/message")
@CrossOrigin(origins = "http://localhost:4200")
public class MessageController {
    private final Logger logger = LoggerFactory.getLogger(MessageController.class);
    private final MessageService messageService;
    private final UserService userService;

    @Autowired
    public MessageController(MessageService messageService, UserService userService) {
        this.messageService = messageService;
        this.userService = userService;
    }

    @PostMapping
    public void postMessage(@RequestBody String data) {
        try {
            JsonReader jsonReader = new JsonReader(data);
            User author = userService.getUserById(jsonReader.readObject("author", User.class).getId());
            Message message = new Message(jsonReader.readText("text"), author);
            messageService.postMessage(message);
            return;
        } catch (IOException e) {
            logger.error("Failed to read JSON data", e);
        }

        throw new IllegalStateException("Failed to post message");
    }

    @GetMapping(value = "/{id}")
    public List<Message> getMessages(@PathVariable("id") Long id) {
        Message message = messageService.getMessageById(id);
        List<Message> messages = new ArrayList<>();
        messages.add(message);
        messages.addAll(message.getAnswers());
        return messages;
    }

    @GetMapping(value = "/{id}/getCountOfLikes")
    public Long getCountOfLikesOfMessage(@PathVariable Long id) {
        return messageService.countLikes(messageService.getMessageById(id));
    }

    @GetMapping(value = "/{id}/getCountOfReblogs")
    public Long getCountOfReblogsOfMessage(@PathVariable Long id) {
        return messageService.countReblogs(messageService.getMessageById(id));
    }

    @PostMapping(value = "/{answeredId}")
    public void answerMessage(@PathVariable Long answeredId, @RequestBody Message message) {
        message.setAuthor(userService.getUserById(message.getAuthor().getId()));
        messageService.answerMessage(messageService.getMessageById(answeredId), message);
    }

    @PostMapping(value = "/{idOfMessage}/like")
    public void likeMessage(@PathVariable Long idOfMessage, @RequestBody String idOfUser) {
        Message message = messageService.getMessageById(idOfMessage);
        User user = userService.getUserById(Long.valueOf(idOfUser));

        messageService.likeMessage(message, user);
    }

    @PostMapping(value = "/{idOfMessage}/dislike")
    public void dislikeMessage(@PathVariable Long idOfMessage, @RequestBody String idOfUser) {
        Message message = messageService.getMessageById(idOfMessage);
        User user = userService.getUserById(Long.valueOf(idOfUser));

        messageService.dislikeMessage(message, user);
    }

    @GetMapping(value = "/{idOfMessage}/isLikedBy/{idOfUser}")
    public Boolean isLiked(@PathVariable Long idOfMessage, @PathVariable Long idOfUser) {
        Message message = messageService.getMessageById(idOfMessage);
        User user = userService.getUserById(idOfUser);

        return messageService.isMessageLiked(message, user);
    }

    @GetMapping(value = "/{idOfMessage}/isRebloggedBy/{idOfUser}")
    public Boolean isReblogged(@PathVariable Long idOfMessage, @PathVariable Long idOfUser) {
        Message message = messageService.getMessageById(idOfMessage);
        User user = userService.getUserById(idOfUser);

        return messageService.isMessageReblogged(message, user);
    }

    @PostMapping(value = "/{idOfMessage}/reblog")
    public void reblog(@PathVariable Long idOfMessage, @RequestBody String idOfUser) {
        Message message = messageService.getMessageById(idOfMessage);
        User user = userService.getUserById(Long.parseLong(idOfUser));

        messageService.reblogMessage(message, user);
    }

    @PostMapping(value = "/{idOfMessage}/removeReblog")
    public void removeReblog(@PathVariable Long idOfMessage, @RequestBody String idOfUser) {
        Message message = messageService.getMessageById(idOfMessage);
        User user = userService.getUserById(Long.parseLong(idOfUser));

        messageService.removeReblog(message, user);
    }
}