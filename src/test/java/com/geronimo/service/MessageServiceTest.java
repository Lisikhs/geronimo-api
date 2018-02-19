package com.geronimo.service;

import com.geronimo.GeronimoSpringTest;
import com.geronimo.model.Message;
import com.geronimo.model.User;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

import static org.junit.Assert.*;


@RunWith(SpringRunner.class)
@GeronimoSpringTest
public class MessageServiceTest {

    @Autowired
    private IMessageService messageService;

    @Autowired
    private IUserService userService;

    @PersistenceContext
    private EntityManager entityManager;

    private User author;
    private User otherUser;

    @Before
    @Transactional
    public void before() {
        userService.deleteUserByUsername("nice_user");
        userService.deleteUserByUsername("not_nice_user");
        entityManager.flush();

        author = userService.saveOrUpdateUser(new User("nice_user", "nice_password"));
        otherUser = userService.saveOrUpdateUser(new User("not_nice_user", "not_nice_password"));
    }

    @After
    @Transactional
    public void after() {
        userService.deleteUserById(otherUser.getId());
        userService.deleteUserById(author.getId());
        entityManager.flush();
    }

    @Transactional
    @Test
    public void postMessage() {
        Message message = new Message("some nice message", author);
        messageService.postMessage(message);

        assertEquals(message, messageService.getMessageById(message.getId()));

        messageService.deleteMessage(message.getId());
        entityManager.flush();
    }

    @Test
    @Transactional
    public void answerMessage() {
        Message originalMessage = new Message("text", author);
        originalMessage = messageService.postMessage(originalMessage);

        Message answerMessage = new Message("this butt sux!", author);
        messageService.answerMessage(originalMessage, answerMessage);

        answerMessage = messageService.getMessageById(answerMessage.getId());

        assertTrue(originalMessage.getAnswers().contains(answerMessage));

        messageService.deleteMessage(originalMessage.getId());
        entityManager.flush();
    }

    @Test
    @Transactional
    public void listFeedMessages() {
        userService.followUser(otherUser.getId(), author.getId());

        Message message1 = new Message("1", author);
        messageService.postMessage(message1);

        Message message2 = new Message("2", author);
        messageService.postMessage(message2);

        Page<Message> messages = messageService.listFeedMessages(otherUser,
                new PageRequest(0, 10, Sort.Direction.DESC, "dateCreated"));

        assertEquals(messages.getTotalPages(), 1);
        assertEquals(messages.getTotalElements(), 2L);

        messageService.deleteMessage(message1.getId());
        messageService.deleteMessage(message2.getId());
        entityManager.flush();
    }

    @Test
    @Transactional
    public void listUserMessagesAndReblogs() {
        Message message1 = new Message("1", author);
        messageService.postMessage(message1);

        Message message2 = new Message("2", otherUser);
        messageService.postMessage(message2);

        message2.addReblog(author);

        Page<Message> messages = messageService.listUserMessagesAndReblogs(author,
                new PageRequest(0, 10,
                        new Sort(new Sort.Order(Sort.Direction.DESC, "dateCreated"))));

        assertEquals(messages.getTotalPages(), 1);
        assertEquals(messages.getTotalElements(), 2L);

        List<Message> messagesFeed = messages.getContent();

        assertTrue(messagesFeed.contains(message1));
        assertTrue(messagesFeed.contains(message2));

        messageService.deleteMessage(message1.getId());
        messageService.deleteMessage(message2.getId());
        entityManager.flush();
    }

    @Test
    @Transactional
    public void likeMessage() {
        Message message = new Message("text", author);
        message = messageService.postMessage(message);
        message = messageService.likeMessage(message, author);

        assertTrue(message.getLikes().contains(author));

        messageService.deleteMessage(message);
        entityManager.flush();
    }

    @Test
    @Transactional
    public void dislikeMessage() {
        Message message = new Message("text", author);
        message = messageService.postMessage(message);
        message = messageService.likeMessage(message, otherUser);
        message = messageService.dislikeMessage(message, otherUser);

        assertFalse(message.getLikes().contains(otherUser));

        messageService.deleteMessage(message);
        entityManager.flush();
    }

    @Test
    @Transactional
    public void isMessageLiked() {
        Message message = messageService.postMessage(new Message("text", author));
        message = messageService.likeMessage(message, otherUser);

        assertTrue(messageService.isMessageLiked(message, otherUser));

        messageService.deleteMessage(message);
        entityManager.flush();
    }

    @Test
    @Transactional
    public void countLikes() {
        Message message = new Message("text", author);
        message = messageService.postMessage(message);
        message = messageService.likeMessage(message, author);

        Long likesCount = messageService.countLikes(message);
        assertEquals(likesCount, Long.valueOf(message.getLikes().size()));
        assertEquals(likesCount.longValue(), 1L);

        messageService.deleteMessage(message);
        entityManager.flush();
    }

    @Test
    @Transactional
    public void reblogMessage() {
        Message message = new Message("1", author);
        message = messageService.postMessage(message);
        message = messageService.reblogMessage(message, otherUser);

        assertTrue(message.getReblogs().contains(otherUser));

        messageService.deleteMessage(message.getId());
        entityManager.flush();
    }

    @Test
    @Transactional
    public void removeReblog() {
        Message message = new Message("1", author);
        message = messageService.postMessage(message);
        message = messageService.reblogMessage(message, otherUser);
        message = messageService.removeReblog(message, otherUser);

        assertFalse(message.getReblogs().contains(otherUser));

        messageService.deleteMessage(message.getId());
        entityManager.flush();
    }

    @Test
    @Transactional
    public void isMessageReblogged() {
        Message message = messageService.postMessage(new Message("text", author));
        message = messageService.reblogMessage(message, otherUser);

        assertTrue(messageService.isMessageReblogged(message, otherUser));

        messageService.deleteMessage(message);
        entityManager.flush();
    }

    @Test
    @Transactional
    public void countReblogs() {
        Message message = new Message("text", author);
        message = messageService.postMessage(message);
        message = messageService.reblogMessage(message, otherUser);

        Long reblogsCount = messageService.countReblogs(message);
        assertEquals(reblogsCount, Long.valueOf(message.getReblogs().size()));
        assertEquals(reblogsCount.longValue(), 1L);

        messageService.deleteMessage(message);
        entityManager.flush();
    }
}
