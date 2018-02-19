package com.geronimo.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.geronimo.JsonReader;
import com.geronimo.model.Message;
import com.geronimo.model.User;
import com.geronimo.service.MessageService;
import com.geronimo.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/users")
@CrossOrigin(origins = "http://localhost:4200")
public class UserController {
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);
    private final UserService userService;
    private final MessageService messageService;

    @Autowired
    UserController(UserService userService, MessageService messageService) {
        this.userService = userService;
        this.messageService = messageService;
    }

    @GetMapping(value = "/{id}")
    public User getUser(@PathVariable("id") Long id) {
        return userService.getUserById(id);
    }

    @GetMapping(value = "/{id}/feed")
    public Message[] getFeed(@PathVariable("id") Long id) {
        User user = userService.getUserById(id);

        List<Message> userMessages = messageService.listUserMessagesAndReblogs(user, new PageRequest(0, 10)).getContent();
        List<Message> content = messageService.listFeedMessages(user, new PageRequest(0, 10)).getContent();

        List<Message> feed = new ArrayList<>(userMessages);
        feed.addAll(content);
        return feed.toArray(new Message[feed.size()]);
    }

    @PutMapping
    public User updateUser(@RequestBody String data) {
        try {
            User updated = new JsonReader(data).readObject(User.class);

            User user = userService.getUserById(updated.getId());
            user.setUsername(updated.getUsername());
            user.setProfile(updated.getProfile());
            return userService.saveOrUpdateUser(user);
        } catch (IOException e) {
            logger.error("Failed to read JSON data", e);
        }

        throw new IllegalStateException("Failed to update user");
    }

    @DeleteMapping(value = "/{id}")
    public void deleteUser(@PathVariable Long id) {
        userService.deleteUser(userService.getUserById(id));
    }

    @GetMapping(value = "/{id}/messages")
    public Message[] getMessagesOfUser(@PathVariable("id") Long id) {
        List<Message> content = messageService.listUserMessagesAndReblogs(userService.getUserById(id),
                new PageRequest(0, 10)).getContent();
        return content.toArray(new Message[content.size()]);
    }

    @GetMapping(value = "/{firstId}/subscribedTo/{secondId}")
    public String isSubscribedTo(@PathVariable("firstId") Long firstUserId, @PathVariable("secondId") Long secondUserId) {
        Boolean isSubscribed = userService.isSubscribedToUser(firstUserId, secondUserId);
        return new ObjectMapper().createObjectNode().put("isSubscribed", isSubscribed).toString();
    }

    @GetMapping(value = "/{firstId}/subscribeTo/{secondId}")
    public void subscribeTo(@PathVariable("firstId") Long firstId,
                            @PathVariable("secondId") Long secondId) {
        userService.followUser(firstId, secondId);
    }

    @GetMapping(value = "/{firstId}/unsubscribeFrom/{secondId}")
    public void unsubscribeFrom(@PathVariable("firstId") Long firstId,
                                  @PathVariable("secondId") Long secondId) {
        userService.unfollowUser(firstId, secondId);
    }

    @GetMapping(value = "/{id}/details")
    public String getUserInfo(@PathVariable("id") Long id) {
        return new ObjectMapper().createObjectNode()
                .put("countOfMessages", userService.getMessageCountOfUser(id))
                .put("countOfFollowed", userService.getCountOfFollowedUsers(id))
                .put("countOfFollowers", userService.getCountOfFollowingUsers(id))
                .toString();

    }

}
