package com.geronimo;

import com.geronimo.dao.UserRepository;
import com.geronimo.model.Message;
import com.geronimo.model.User;
import com.geronimo.service.MessageService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MessageServiceTest {

    private MessageService messageService;
    private UserRepository userRepository;

    @Test
    public void testMessageShouldBeStoredAndRetrieved() {
        User user = userRepository.findByUsername("firstUsername");

        Message message = new Message("firstUsername");
        messageService.postMessage(message, user);

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
    public void testReblogMessageServiceMethod() {
        User user = userRepository.findByUsername("firstUsername");
        Message messageToReblog = messageService.getMessageById(5L);

        messageService.reblogMessage(messageToReblog, user);

        Message rebloggedMessage = messageService.getMessageById(5L);
        assertTrue(rebloggedMessage.getReblogs().contains(user));
    }

    @Test
    @Transactional
    public void testAnswerMessageServiceMethod() {
        User user = userRepository.findByUsername("firstUsername");
        Message newMessage = new Message("this is an answer");
        Message toBeAnswered = messageService.getMessageById(5L);

        messageService.answerMessage(toBeAnswered, newMessage, user);

        Message answer = messageService.getMessageById(newMessage.getId());
        Message answered = messageService.getMessageById(5L);

        assertTrue(answered.getAnswers().contains(answer));

        assertEquals(answer.getId(), newMessage.getId());
        assertEquals(answer.getText(), newMessage.getText());
        assertEquals(answer.getAuthor().getId(), newMessage.getAuthor().getId());

        assertNotNull(answer.getDateCreated());
        assertNotNull(answer.getLastUpdated());
        assertEquals(answer.getVersion().longValue(), 0L);

        messageService.deleteMessage(answer.getId());
    }

    @Autowired
    public void setMessageService(MessageService messageService) {
        this.messageService = messageService;
    }

    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

}
