package com.geronimo;

import com.geronimo.dao.MessageRepository;
import com.geronimo.dao.UserRepository;
import com.geronimo.model.Message;
import com.geronimo.model.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import java.math.BigInteger;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
public class GeronimoDatabaseTest {

    private EntityManagerFactory factory;
    private MessageRepository messageRepository;
    private UserRepository userRepository;

    @Test
    public void testDatabaseConnectionEstablished() {
        EntityManager entityManager = factory.createEntityManager();
        Query nativeQuery = entityManager.createNativeQuery("SELECT 1");
        assertEquals(nativeQuery.getSingleResult(), BigInteger.valueOf(1L));
    }

    @Test
    public void testMessageAndAuthor() {
        User user = new User();
        user.setUsername("username");
        user.setPassword("password");
        userRepository.save(user);

        Message message = new Message();
        message.setText("message");
        message.setAuthor(user);
        messageRepository.save(message);

        user.getMessages().add(message);
        userRepository.save(user);
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
