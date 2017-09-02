package com.geronimo;

import com.geronimo.model.Message;
import com.geronimo.model.User;
import com.geronimo.service.IMessageService;
import com.geronimo.service.IUserService;
import com.geronimo.util.DateUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
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
@SpringBootTest
public class MessageServiceTest {

    private IMessageService messageService;
    private IUserService userService;
    private EntityManager entityManager;

    private User author;

    @Transactional
    @Before
    public void before() {
        userService.deleteUserByUsername("nice_user");
        entityManager.flush();

        author = userService.saveUser(new User("nice_user", "nice_password"));
    }

    @Transactional
    @After
    public void after() {
        userService.deleteUser(author);
        entityManager.flush();
    }

    @Test
    @Transactional
    public void testListFeedMessagesServiceMethod() throws InterruptedException {
        // TODO: when test fails, user will not be deleted, you could move creation/deletion to before/after
        userService.deleteUserByUsername("not_nice_user");
        entityManager.flush();

        User badUser = new User("not_nice_user", "neither_nice_password");
        userService.saveUser(badUser);

        userService.followUser(badUser, author);
        entityManager.flush();

        Message message1 = new Message("wow that's my first message!", author);
        messageService.postMessage(message1);
        entityManager.flush();

        Message message2 = new Message("oh wow, here comes the second", author);
        messageService.postMessage(message2);
        entityManager.flush();

        Page<Message> messages = messageService.listFeedMessages(badUser,
                new PageRequest(0, 10,
                new Sort(new Sort.Order(Sort.Direction.DESC, "dateCreated"))));

        assertEquals(messages.getTotalPages(), 1);
        assertEquals(messages.getTotalElements(), 2L);

        List<Message> messagesFeed = messages.getContent();

        long dateCreated1Millis = DateUtils.getLocalDateTimeMillis(messagesFeed.get(0).getDateCreated());
        long dateCreated2Millis = DateUtils.getLocalDateTimeMillis(messagesFeed.get(1).getDateCreated());

        // TODO: figure out why sometimes the first item has a bigger amount of millis,
        // TODO: is the sorting in query doesn't work?
        // TODO: and remove println lines of code
        System.out.println(dateCreated1Millis);
        System.out.println(dateCreated2Millis);
        assertTrue(dateCreated1Millis >= dateCreated2Millis);

        userService.deleteUserById(badUser.getId());
        messageService.deleteMessage(message1.getId());
        messageService.deleteMessage(message2.getId());
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
        messageService.reblogMessage(rebloggableMessage, author);

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

    @Autowired
    public void setMessageService(IMessageService messageService) {
        this.messageService = messageService;
    }

    @Autowired
    public void setUserService(IUserService userService) {
        this.userService = userService;
    }

    @PersistenceContext
    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }
}
