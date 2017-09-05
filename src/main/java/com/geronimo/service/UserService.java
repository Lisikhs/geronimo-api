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
    public void followUser(User follower, User following) {
        Validate.notNull(follower);
        Validate.notNull(following);

        if (follower.equals(following)) {
            throw new SelfFollowingException("User with username '" +
                    follower.getUsername() + "' tried to follow himself");
        }
        following.addFollower(follower);

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

    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
}
