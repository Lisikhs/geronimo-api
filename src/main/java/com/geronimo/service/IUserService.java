package com.geronimo.service;

import com.geronimo.model.User;

import java.util.List;

public interface IUserService {

    User saveOrUpdateUser(User user);

    void followUser(Long followerId, Long followingId);

    void unfollowUser(Long followerId, Long followingId);

    List<User> getFollowingUsers(Long id);

    User getUserByUsername(String username);

    User getUserByUsernameAndPassword(String username, String password);

    User getUserById(Long id);

    void deleteUser(User user);

    void deleteUserById(Long id);

    void deleteUserByUsername(String username);

    Boolean isSubscribedToUser(Long userId, Long anotherUserId);

    Integer getMessageCountOfUser(Long userId);

    Integer getCountOfFollowedUsers(Long id);

    Integer getCountOfFollowingUsers(Long id);
}
