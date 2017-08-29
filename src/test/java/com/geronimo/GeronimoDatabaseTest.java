package com.geronimo;

import com.geronimo.dao.MessageRepository;
import com.geronimo.dao.UserRepository;
import com.geronimo.model.Message;
import com.geronimo.model.Profile;
import com.geronimo.model.User;
import org.hibernate.Hibernate;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import java.math.BigInteger;
import java.util.Date;

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
        user.setProfile(new Profile("status", new Date()));
        user = userRepository.save(user);  //INSERT

        Message message = new Message();
        message.setText("message");
        message.setAuthor(user);
        message = messageRepository.save(message); //INSERT

        user.getMessages().add(message);
        user = userRepository.save(user);//UPDATE

        User anotherUser = new User();
        anotherUser.setUsername("anotherUsername");
        anotherUser.setPassword("anotherPassword");
        anotherUser.setProfile(new Profile("new status", new Date()));
        anotherUser = userRepository.save(anotherUser); //INSERT

        user.follow(anotherUser);
        user = userRepository.save(user);
    }

    @Test
    public void testRetrievingData() {
        EntityManager entityManager = factory.createEntityManager();
        User firstUser = (User) entityManager.createQuery("select u from User u WHERE u.id = 1").getSingleResult();

        Hibernate.initialize(firstUser.getMessages());
        System.out.println(firstUser.getMessages());

        entityManager.close();
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
