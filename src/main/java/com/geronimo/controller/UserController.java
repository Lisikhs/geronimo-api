package com.geronimo.controller;

import com.geronimo.model.Message;
import com.geronimo.model.User;
import com.geronimo.service.FakeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/users")
public class UserController {

    private final FakeService fakeService;

    @Autowired
    UserController(FakeService fakeService) {
        this.fakeService = fakeService;
    }

    @GetMapping
    public List<User> getUsers() {
        return FakeService.getUsers();
    }

    @GetMapping(value = "/{id}")
    public User getUser(@PathVariable("id") Long id) {
        return fakeService.getUserById(id);
    }

    @GetMapping(value = "/{id}/feed")
    public List<Message> getFeed(@PathVariable("id") Long id) {
        return fakeService.getFeedByUserId(id);
    }

    @PostMapping
    public Boolean saveUser(@RequestBody User user) {
        return fakeService.saveUser(user);
    }

    @PostMapping(value = "/validate")
    public User validateUser(@RequestBody User user) {
        return fakeService.validateUser(user);
    }

    @PutMapping
    public User updateUser(@RequestBody User user) {
        return fakeService.updateUser(user);
    }

    @DeleteMapping(value = "/{id}")
    public boolean deleteUser(@PathVariable Long id) {
        return fakeService.deleteUserById(id);
    }

    @GetMapping(value = "/{id}/messages")
    public List<Message> getMessagesOfUser(@PathVariable("id") Long id) {
        return fakeService.getMessagesByUserId(id);
    }

    @GetMapping(value = "/is/{idOfSessionUser}/subscribedTo/{idOfUser}")
    public Boolean isSubscribedTo(@PathVariable("idOfSessionUser") Long idOfSessionUser,
                                        @PathVariable("idOfUser") Long idOfUser) {
        return fakeService.isSubscribedTo(idOfSessionUser, idOfUser);
    }

    @GetMapping(value = "/{idOfSessionUser}/subscribeTo/{idOfUser}")
    public void subscribeTo(@PathVariable("idOfSessionUser") Long idOfSessionUser,
                                  @PathVariable("idOfUser") Long idOfUser) {
        fakeService.subscribeTo(idOfSessionUser, idOfUser);
    }

    @GetMapping(value = "/{idOfSessionUser}/unsubscribeFrom/{idOfUser}")
    public void unsubscribeFrom(@PathVariable("idOfSessionUser") Long idOfSessionUser,
                                  @PathVariable("idOfUser") Long idOfUser) {
        fakeService.unsubscribeFrom(idOfSessionUser, idOfUser);
    }

    @GetMapping(value = "/getUserInfo/{idOfUser}")
    public int[] getUserInfo(@PathVariable("idOfUser") Long idOfUser) {
        return fakeService.getUserInfo(idOfUser);
    }

}
