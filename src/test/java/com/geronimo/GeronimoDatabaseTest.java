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
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import java.math.BigInteger;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class GeronimoDatabaseTest {

    private EntityManager entityManager;
    private UserRepository userRepository;

    @Transactional
    @Test
    public void testDatabaseConnectionEstablished() {
        Query nativeQuery = entityManager.createNativeQuery("SELECT 1");
        assertEquals(nativeQuery.getSingleResult(), BigInteger.valueOf(1L));
    }

    @Test
    @Transactional
    public void testUserCouldBeStoredAndRetrieved() {
        User firstUser = new User("firstUsername", "firstPassword");
        firstUser = userRepository.save(firstUser);

        User retrievedUser = userRepository.findByUsername("firstUsername");
        assertEquals(retrievedUser.getId(), firstUser.getId());
        assertEquals(retrievedUser.getUsername(), firstUser.getUsername());
        assertEquals(retrievedUser.getPassword(), firstUser.getPassword());
        assertEquals(retrievedUser.getProfile(), firstUser.getProfile());
        assertNotNull(retrievedUser.getDateCreated());
        assertNotNull(retrievedUser.getLastUpdated());
        assertEquals(retrievedUser.getVersion().longValue(), 0L);
    }

    @PersistenceContext
    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
}
