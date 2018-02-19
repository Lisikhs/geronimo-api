package com.geronimo.service;

import com.geronimo.dao.UserRepository;
import com.geronimo.exception.SelfFollowingException;
import com.geronimo.model.User;
import org.apache.commons.lang3.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class UserService implements IUserService {

    private UserRepository userRepository;

    @Transactional
    @Override
    public User saveOrUpdateUser(User user) {
        Validate.notNull(user);

        return userRepository.save(user);
    }

    @Transactional
    @Override
    public void followUser(Long followerId, Long followingId) {
        Validate.notNull(followerId);
        Validate.notNull(followingId);

        if (followerId.equals(followingId)) {
            throw new SelfFollowingException("User with id '" + followerId + "' tried to follow oneself");
        }

        User follower = getUserById(followerId);
        User following = getUserById(followingId);

        following.addFollower(follower);
        saveOrUpdateUser(following);
    }

    @Transactional
    @Override
    public void unfollowUser(Long followerId, Long followingId) {
        Validate.notNull(followerId);
        Validate.notNull(followingId);

        if (followerId.equals(followingId)) {
            throw new SelfFollowingException("User with id '" + followerId + "' tried to unfollow oneself");
        }

        User follower = getUserById(followerId);
        User following = getUserById(followingId);

        following.removeFollower(follower);
        saveOrUpdateUser(following);
    }


    @Override
    public List<User> getFollowingUsers(Long id) {
        Validate.notNull(id);

        return userRepository.getFollowingUsers(id);
    }

    @Override
    public User getUserByUsername(String username) {
        Validate.notNull(username);

        return userRepository.findByUsername(username);
    }

    @Override
    public User getUserById(Long id) {
        Validate.notNull(id);

        return userRepository.findOne(id);
    }

    @Override
    public User getUserByUsernameAndPassword(String username, String password) {
        Validate.notNull(username);
        Validate.notNull(password);

        return userRepository.findByUsernameAndPassword(username, password);
    }

    @Transactional
    @Override
    public void deleteUser(User user) {
        Validate.notNull(user);

        userRepository.delete(user);
    }

    @Transactional
    @Override
    public void deleteUserById(Long id) {
        Validate.notNull(id);

        userRepository.delete(id);
    }

    @Transactional
    @Override
    public void deleteUserByUsername(String username) {
        Validate.notNull(username);

        userRepository.deleteByUsername(username);
    }

    @Transactional
    @Override
    public Boolean isSubscribedToUser(Long userId, Long anotherUserId) {
        Validate.notNull(userId);
        Validate.notNull(anotherUserId);

        return userRepository.isUserSubscribedToAnotherUser(userId, userRepository.findOne(anotherUserId));
    }

    @Transactional
    @Override
    public Integer getMessageCountOfUser(Long userId) {
        Validate.notNull(userId);

        return userRepository.getCountOfMessages(userId);
    }

    @Transactional
    @Override
    public Integer getCountOfFollowedUsers(Long id) {
        Validate.notNull(id);

        return userRepository.getCountOfFollowedUsers(id);
    }

    @Transactional
    @Override
    public Integer getCountOfFollowingUsers(Long id) {
        Validate.notNull(id);

        return userRepository.getCountOfFollowingUsers(id);
    }

    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
}
