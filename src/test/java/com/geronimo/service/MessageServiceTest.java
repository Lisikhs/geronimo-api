package com.geronimo.service;

import com.geronimo.GeronimoSpringTest;
import com.geronimo.model.Message;
import com.geronimo.model.User;
import com.geronimo.util.DateUtils;
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
import java.util.Arrays;
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

    @Transactional
    @Before
    public void before() {
        userService.deleteByUsername("nice_user");
        userService.deleteByUsername("not_nice_user");
        entityManager.flush();

        author = userService.saveOrUpdateUser(new User("nice_user", "nice_password"));
        otherUser = userService.saveOrUpdateUser(new User("not_nice_user", "neither_nice_password"));
    }

    @Test
    @Transactional
    public void testListFeedMessagesServiceMethod()  {
        userService.followUser(otherUser, author);
        entityManager.flush();

        Message message1 = new Message("wow that's my first message!", author);
        messageService.postMessage(message1);
        entityManager.flush();

        Message message2 = new Message("oh wow, here comes the second", author);
        messageService.postMessage(message2);
        entityManager.flush();


        Page<Message> messages = messageService.listFeedMessages(otherUser,
                new PageRequest(0, 10,
                null));

        assertEquals(messages.getTotalPages(), 1);
        assertEquals(messages.getTotalElements(), 2L);
        assertTrue(messages.getContent().containsAll(Arrays.asList(message1, message2)));
    }


    @Test
    @Transactional
    public void testListUserMessagesAndReblogs() {
        Message message1 = new Message("1", author);
        messageService.postMessage(message1);

        Message message2 = new Message("2", otherUser);
        messageService.postMessage(message2);

        Message message3 = new Message("3", author);
        messageService.postMessage(message3);

        Message message4 = new Message("4", otherUser);
        messageService.postMessage(message4);

        message2.addReblog(author);
        message4.addReblog(author);

        Page<Message> messages = messageService.listUserMessagesAndReblogs(author,
                new PageRequest(0, 10,
                        new Sort(new Sort.Order(Sort.Direction.DESC, "dateCreated"))));

        assertEquals(messages.getTotalPages(), 1);
        assertEquals(messages.getTotalElements(), 4L);


        List<Message> messagesFeed = messages.getContent();

        assertTrue(messagesFeed.contains(message1));
        assertTrue(messagesFeed.contains(message2));
        assertTrue(messagesFeed.contains(message3));
        assertTrue(messagesFeed.contains(message4));
    }

    @Test
    @Transactional
    public void testReblogMessageServiceMethod() {
        Message message = new Message("hello, dudes", author);
        messageService.postMessage(message);

        Message messageToReblog = messageService.getMessageById(message.getId());
        messageService.reblogMessage(messageToReblog, otherUser);

        Message rebloggedMessage = messageService.getMessageById(messageToReblog.getId());
        assertTrue(rebloggedMessage.getReblogs().contains(otherUser));

        messageService.deleteMessage(message.getId());
        entityManager.flush();
    }

    @Test
    @Transactional
    public void testLikeMessageServiceMethod() {
        Message message = new Message("oh wow", author);
        messageService.postMessage(message);

        // yes, I like my own messages, why not? who doesn't do that?!
        messageService.likeMessage(message, author);
        assertTrue(message.getLikes().contains(author));

        messageService.deleteMessage(message);
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
    public void testCountLikesServiceMethod() {
        Message likeableMessage = new Message("I'm sexy and I know it", author);
        messageService.postMessage(likeableMessage);

        // again, why can't I just like my message? seriously?!
        messageService.likeMessage(likeableMessage, author);

        Long likesCount = messageService.countLikes(likeableMessage);
        assertEquals(likesCount, Long.valueOf(likeableMessage.getLikes().size()));
        assertEquals(likesCount.longValue(), 1L);

        messageService.deleteMessage(likeableMessage);
        entityManager.flush();
    }

    @Test
    @Transactional
    public void testCountReblogsServiceMethod() {
        Message rebloggableMessage = new Message("I'm sexy and I know it", author);
        messageService.postMessage(rebloggableMessage);

        // again, why can't I just like my message? seriously?!
        messageService.reblogMessage(rebloggableMessage, otherUser);

        Long reblogsCount = messageService.countReblogs(rebloggableMessage);
        assertEquals(reblogsCount, Long.valueOf(rebloggableMessage.getReblogs().size()));
        assertEquals(reblogsCount.longValue(), 1L);

        messageService.deleteMessage(rebloggableMessage);
        entityManager.flush();
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

        messageService.deleteMessage(answer.getId()); //besauce of cascading may not be deleted manually
        messageService.deleteMessage(originalMessage.getId());
        entityManager.flush();
    }
}
