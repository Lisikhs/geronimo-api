package com.geronimo.service;

import com.geronimo.model.User;

import java.util.List;

public interface IUserService {

    User saveOrUpdateUser(User user);

    void followUser(User follower, User following);

    List<User> getFollowingUsers(Long id);

    User getByUsername(String username);

    User getById(Long id);

    User getCurrent();

    void deleteById(Long id);

    void deleteByUsername(String username);
}
