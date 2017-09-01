package com.geronimo.service;

import com.geronimo.dao.MessageRepository;
import com.geronimo.exceptions.RebloggingOwnMessageException;
import com.geronimo.model.Message;
import com.geronimo.model.User;
import org.apache.commons.lang3.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional(readOnly = true)
public class MessageService implements IMessageService {

    private MessageRepository messageRepository;
    private IUserService userService;

    @Transactional
    @Override
    public Page<Message> listFeedMessages(User sessionUser, Pageable pageable) {
        return messageRepository.findByAuthorInOrderByDateCreatedDesc(userService.getFollowingUsers(sessionUser.getId()), pageable);
    }

    @Override
    @Transactional
    public void saveOrUpdateMessage(Message message) {
        Validate.notNull(message);

        messageRepository.save(message);
    }

    @Transactional
    @Override
    public void reblogMessage(Message messageToReblog, User whoReblogged) {
        Validate.notNull(messageToReblog);
        Validate.notNull(whoReblogged);

        if (messageToReblog.getAuthor().equals(whoReblogged)) {
            throw new RebloggingOwnMessageException("User with username \'" +
                    whoReblogged.getUsername() + "\' tried to reblog his own messages");
        }

        messageToReblog.addReblog(whoReblogged);
        saveOrUpdateMessage(messageToReblog);
    }

    @Transactional
    @Override
    public void postMessage(Message message) {
        Validate.notNull(message);

        saveOrUpdateMessage(message);
    }

    @Transactional
    @Override
    public void likeMessage(Message message, User whoLiked) {
        Validate.notNull(message);
        Validate.notNull(whoLiked);

        message.addLike(whoLiked);
        saveOrUpdateMessage(message);
    }

    @Override
    public Long countLikes(Message message) {
        Validate.notNull(message);

        return messageRepository.countLikes(message.getId());
    }

    @Override
    public Long countReblogs(Message message) {
        Validate.notNull(message);

        return messageRepository.countReblogs(message.getId());
    }

    @Transactional
    @Override
    public void answerMessage(Message message, Message reply) {
        Validate.notNull(message);
        Validate.notNull(reply);

        saveOrUpdateMessage(reply);

        message.addAnswer(reply);
        saveOrUpdateMessage(message);
    }

    @Override
    public Message getMessageById(Long id) {
        Validate.notNull(id);

        return messageRepository.findOne(id);
    }

    @Transactional
    @Override
    public void deleteMessage(Long id) {
        Validate.notNull(id);

        messageRepository.delete(id);
    }

    @Transactional
    @Override
    public void deleteMessage(Message message) {
        Validate.notNull(message);

        messageRepository.delete(message);
    }

    @Autowired
    public void setMessageRepository(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    @Autowired
    public void setUserService(IUserService userService) {
        this.userService = userService;
    }
}
