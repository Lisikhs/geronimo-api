package com.geronimo.service;

import com.geronimo.GeronimoSpringTest;
import com.geronimo.model.User;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@GeronimoSpringTest
public class UserServiceTest {
    private UserService userService;
    private EntityManager entityManager;

    private User user1;
    private User user2;
    private User user3;

    @Transactional
    @Before
    public void before() {
        userService.deleteUserByUsername("user1");
        userService.deleteUserByUsername("user2");
        userService.deleteUserByUsername("user3");
        entityManager.flush();

        user1 = userService.saveOrUpdateUser(new User("user1", "nice_password_1"));
        user2 = userService.saveOrUpdateUser(new User("user2", "nice_password_2"));
        user3 = userService.saveOrUpdateUser(new User("user3", "nice_password_3"));
    }

    @Transactional
    @After
    public void after() {
        userService.deleteUserById(user1.getId());
        userService.deleteUserById(user2.getId());
        userService.deleteUserById(user3.getId());
        entityManager.flush();
    }

    @Transactional
    @Test
    public void getUserById() {
        Assert.assertEquals(userService.getUserById(user1.getId()), user1);
    }

    @Transactional
    @Test
    public void getUserByUsername() {
        Assert.assertEquals(userService.getUserByUsername(user1.getUsername()), user1);
    }

    @Transactional
    @Test
    public void saveUser() {
        User user = new User("username", "password");
        user = userService.saveOrUpdateUser(user);

        assertEquals(user, userService.getUserByUsername(user.getUsername()));

        userService.deleteUser(user);
        entityManager.flush();
    }

    @Transactional
    @Test
    public void followUser() {
        user1.addFollower(user2);
        user1.addFollower(user3);
        user1 = userService.saveOrUpdateUser(user1);

        List<User> followedUsers = userService.getFollowingUsers(user2.getId());

        assertEquals(2, user1.getFollowers().size());
        assertTrue(followedUsers.contains(user1));
        assertFalse(userService.isSubscribedToUser(user1.getId(), user3.getId()));
    }

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @PersistenceContext
    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }
}
