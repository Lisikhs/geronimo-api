package com.geronimo.service;

import com.geronimo.model.User;

import java.util.List;

public interface IUserService {

    User saveOrUpdateUser(User user);

    void followUser(User follower, User following);

    List<User> getFollowingUsers(Long id);

    User getUser(String username);

    User getUser(Long id);

    void deleteUser(User user);

    void deleteUser(Long id);

    void deleteUser(String username);

    User saveUser(User user);
}
