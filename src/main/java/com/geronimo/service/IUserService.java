package com.geronimo.service;

import com.geronimo.model.User;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface IUserService {

    User saveOrUpdateUser(User user);

    void followUser(User follower, User following);

    List<User> getFollowingUsers(Long id);

    User getUserByUsername(String username);

    User getUserById(Long id);

    void deleteUser(User user);

    void deleteUserById(Long id);

    void deleteUserByUsername(String username);

    User saveUser(User user);
}
