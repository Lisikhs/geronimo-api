package com.geronimo.repository;

import com.geronimo.GeronimoSpringTest;
import com.geronimo.dao.UserRepository;
import com.geronimo.model.User;
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
public class UserRepositoryTest {

    private UserRepository userRepository;
    private EntityManager entityManager;

    @Transactional
    @Test
    public void testUserCouldBeStoredAndRetrieved() {
        User firstUser = new User("firstUsername", "firstPassword");
        firstUser = userRepository.save(firstUser);

        User retrievedUser = userRepository.findByUsername("firstUsername");
        assertEquals(retrievedUser.getId(), firstUser.getId());
        assertEquals(retrievedUser.getUsername(), firstUser.getUsername());
        assertEquals(retrievedUser.getPassword(), firstUser.getPassword());
        assertNotNull(retrievedUser.getDateCreated());
        assertNotNull(retrievedUser.getLastUpdated());
        assertEquals(retrievedUser.getVersion().longValue(), 0L);

        userRepository.delete(firstUser);
        entityManager.flush();
    }

    @Transactional
    @Test
    public void testUserFollowingRelationship() {
        User followed = new User("followed", "ohmygosh");
        User follower1 = new User("follower1", "ohmygosh");
        User follower2 = new User("follower2", "ohmygosh");
        userRepository.save(followed);
        userRepository.save(follower1);
        userRepository.save(follower2);

        followed.addFollower(follower1);
        followed.addFollower(follower2);
        userRepository.save(followed);
        entityManager.flush();

        User followedUpdated = userRepository.findByUsername("followed");
        assertEquals(followedUpdated.getFollowers().size(), 2);

        List<User> followedUsers = userRepository.getFollowingUsers(follower1.getId());
        assertTrue(followedUsers.contains(followed));

        userRepository.deleteByUsername("followed");
        userRepository.deleteByUsername("follower1");
        userRepository.deleteByUsername("follower2");
        entityManager.flush();
    }

    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @PersistenceContext
    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }
}
