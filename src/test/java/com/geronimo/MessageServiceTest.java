package com.geronimo;

import com.geronimo.dao.UserRepository;
import com.geronimo.model.Message;
import com.geronimo.model.User;
import com.geronimo.service.MessageService;
import org.junit.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MessageServiceTest {

    private MessageService messageService;
    private UserRepository userRepository;
    private EntityManager entityManager;

    private User author;

    @Transactional
    @Before
    public void before() {
        userRepository.deleteByUsername("nice_user");
        entityManager.flush();

        author = userRepository.save(new User("nice_user", "nice_password"));
    }

    @Transactional
    @After
    public void after() {
        userRepository.delete(author);
        entityManager.flush();
    }

    @Test
    @Transactional
    public void testReblogMessageServiceMethod() {
        Message message = new Message("hello, dudes", author);
        messageService.postMessage(message);

        Message messageToReblog = messageService.getMessageById(message.getId());
        messageService.reblogMessage(messageToReblog, author);

        Message rebloggedMessage = messageService.getMessageById(messageToReblog.getId());
        assertTrue(rebloggedMessage.getReblogs().contains(author));

        messageService.deleteMessage(message.getId());
        entityManager.flush();
    }

    @Transactional
    @Test
    public void testMessageShouldBeStoredAndRetrieved() {
        Message message = new Message("some nice message", author);
        messageService.postMessage(message);

        Message retrievedMessage = messageService.getMessageById(message.getId());

        assertEquals(retrievedMessage.getId(), message.getId());
        assertEquals(retrievedMessage.getText(), message.getText());
        assertEquals(retrievedMessage.getAuthor().getId(), message.getAuthor().getId());


        assertNotNull(retrievedMessage.getDateCreated());
        assertNotNull(retrievedMessage.getLastUpdated());
        assertEquals(retrievedMessage.getVersion().longValue(), 0L);

        messageService.deleteMessage(message.getId());
    }

    @Test
    @Transactional
    public void testAnswerMessageServiceMethod() {
        Message originalMessage = new Message("oh my gosh, look at her butt!", author);
        messageService.postMessage(originalMessage);

        // user replied to his own message (why not?)
        Message answerMessage = new Message("this butt sux!", author);
        messageService.answerMessage(originalMessage, answerMessage);

        Message answered = messageService.getMessageById(originalMessage.getId());
        Message answer = messageService.getMessageById(answerMessage.getId());

        assertTrue(answered.getAnswers().contains(answer));

        assertEquals(answer.getId(), answerMessage.getId());
        assertEquals(answer.getText(), answerMessage.getText());
        assertEquals(answer.getAuthor().getId(), answerMessage.getAuthor().getId());

        assertNotNull(answer.getDateCreated());
        assertNotNull(answer.getLastUpdated());
        assertEquals(answer.getVersion().longValue(), 0L);

        messageService.deleteMessage(answer.getId());
        messageService.deleteMessage(originalMessage.getId());
        entityManager.flush();
    }

    @Autowired
    public void setMessageService(MessageService messageService) {
        this.messageService = messageService;
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
