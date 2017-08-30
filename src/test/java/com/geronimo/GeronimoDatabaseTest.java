package com.geronimo;

import com.geronimo.dao.MessageRepository;
import com.geronimo.dao.UserRepository;
import com.geronimo.model.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import java.math.BigInteger;
import java.util.HashSet;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class GeronimoDatabaseTest {

    private EntityManagerFactory factory;
    private MessageRepository messageRepository;
    private UserRepository userRepository;
    private Logger logger = LoggerFactory.getLogger(GeronimoDatabaseTest.class);

    @Test
    public void testDatabaseConnectionEstablished() {
        EntityManager entityManager = factory.createEntityManager();
        Query nativeQuery = entityManager.createNativeQuery("SELECT 1");
        assertEquals(nativeQuery.getSingleResult(), BigInteger.valueOf(1L));
    }


    @Test
    public void testRetrievingData() {
        EntityManager entityManager = factory.createEntityManager();
        User firstUser = (User) entityManager.createQuery("select u from User u WHERE u.id = 3").getSingleResult();

        System.out.println(firstUser.getMessages());
        entityManager.close();
    }

    @Test
    @Transactional
    public void testUser() {
        User firstUser = new User("firstUsername", "firstPassword");
        userRepository.save(firstUser);

        firstUser = userRepository.findByUsername("firstUsername");
        assertNotNull(firstUser.getId());
        assertNotNull(firstUser.getUsername());
        assertNotNull(firstUser.getPassword());
        assertNotNull(firstUser.getProfile());
        assertNull(firstUser.getProfile().getStatus());
        assertEquals(firstUser.getFollowers(), new HashSet<>());
    }


    @Autowired
    public void setEntityManagerFactory(EntityManagerFactory factory) {
        this.factory = factory;
    }

    @Autowired
    public void setMessageRepository(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
}
