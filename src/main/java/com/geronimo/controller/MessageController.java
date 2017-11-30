package com.geronimo.controller;

import com.geronimo.model.Message;
import com.geronimo.service.FakeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/message")
public class MessageController {

    private final FakeService fakeService;

    @Autowired
    public MessageController(FakeService fakeService) {
        this.fakeService = fakeService;
    }

    @GetMapping(value = "/{id}")
    public List<Message> getMessages(@PathVariable("id") Long id) {
        return fakeService.getMessagesByMessageId(id);
    }

    @GetMapping(value = "/{id}/getCountOfLikes")
    public Long getCountOfLikesOfMessage(@PathVariable Long id) {
        return fakeService.getCountOfLikesByMessageId(id);
    }

    @GetMapping(value = "/{id}/getCountOfReblogs")
    public Long getCountOfReblogsOfMessage(@PathVariable Long id) {
        return fakeService.getCountOfReblogsByMessageId(id);
    }

    @PostMapping(value = "/{answeredId}")
    public Message answerMessage(@PathVariable Long answeredId, @RequestBody Message message) {
       return fakeService.answerMessage(answeredId, message);
    }

    @PostMapping
    public void postMessage(@RequestBody Message message) {
        fakeService.postMessage(message);
        }

    @PostMapping(value = "/{idOfMessage}/like")
    public void likeMessage(@PathVariable Long idOfMessage, @RequestBody String idOfUser) {
        fakeService.likeMessage(idOfMessage, idOfUser);
    }

    @PostMapping(value = "/{idOfMessage}/dislike")
    public void dislikeMessage(@PathVariable Long idOfMessage, @RequestBody String idOfUser) {
        fakeService.dislikeMessage(idOfMessage, idOfUser);
    }

    @GetMapping(value = "/{idOfMessage}/isLikedBy/{idOfUser}")
    public Boolean isLiked(@PathVariable Long idOfMessage, @PathVariable Long idOfUser) {
        return fakeService.isLiked(idOfMessage, idOfUser);
    }

    @GetMapping(value = "/{idOfMessage}/isRebloggedBy/{idOfUser}")
    public Boolean isReblogged(@PathVariable Long idOfMessage, @PathVariable Long idOfUser) {
        return fakeService.isReblogged(idOfMessage, idOfUser);
    }

    @PostMapping(value = "/{idOfMessage}/reblog")
    public void reblog(@PathVariable Long idOfMessage, @RequestBody String idOfUser) {
        fakeService.reblog(idOfMessage, idOfUser);
    }

    @PostMapping(value = "/{idOfMessage}/removeReblog")
    public void removeReblog(@PathVariable Long idOfMessage, @RequestBody String idOfUser) {
        fakeService.removeReblog(idOfMessage, idOfUser);
    }
}