package com.geronimo.service;

import com.geronimo.dao.MessageRepository;
import com.geronimo.exception.RebloggingOwnMessageException;
import com.geronimo.model.Message;
import com.geronimo.model.User;
import org.apache.commons.lang3.Validate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional(readOnly = true)
public class MessageService implements IMessageService {

    private MessageRepository messageRepository;
    private IUserService userService;

    public MessageService(MessageRepository messageRepository, IUserService userService) {
        this.messageRepository = messageRepository;
        this.userService = userService;
    }


    @Transactional
    @Override
    public Message saveOrUpdateMessage(Message message) {
        Validate.notNull(message);

        return messageRepository.save(message);
    }

    @Transactional
    @Override
    public Message postMessage(Message message) {
        Validate.notNull(message);
        Validate.notNull(message.getAuthor());

        return saveOrUpdateMessage(message);
    }

    @Transactional
    @Override
    public Page<Message> listFeedMessages(User currentUser, Pageable pageable) {
        return messageRepository.findByAuthorIn(userService.getFollowingUsers(currentUser.getId()), pageable);
    }

    @Transactional
    @Override
    public Page<Message> listUserMessagesAndReblogs(User author, Pageable pageable) {
        return messageRepository.findUserMessages(author, pageable);
    }

    @Transactional
    @Override
    public Message likeMessage(Message message, User whoLiked) {
        Validate.notNull(message);
        Validate.notNull(whoLiked);

        message.addLike(whoLiked);
        return saveOrUpdateMessage(message);
    }

    @Override
    public Boolean isMessageLiked(Message message, User whoLiked) {
        Validate.notNull(message);
        Validate.notNull(whoLiked);

        return messageRepository.isMessageLiked(message.getId(), whoLiked);
    }

    @Override
    public Long countLikes(Message message) {
        Validate.notNull(message);

        return messageRepository.countLikes(message.getId());
    }

    @Override
    @Transactional
    public Message dislikeMessage(Message message, User whoDisliked) {
        Validate.notNull(message);
        Validate.notNull(whoDisliked);

        message.removeLike(whoDisliked);
        return saveOrUpdateMessage(message);
    }

    @Transactional
    @Override
    public Message reblogMessage(Message messageToReblog, User whoReblogged) {
        Validate.notNull(messageToReblog);
        Validate.notNull(whoReblogged);

        if (messageToReblog.getAuthor().equals(whoReblogged)) {
            throw new RebloggingOwnMessageException("User with username '" +
                    whoReblogged.getUsername() + "' tried to reblog his own messages");
        }

        messageToReblog.addReblog(whoReblogged);
        return saveOrUpdateMessage(messageToReblog);
    }

    @Transactional
    @Override
    public Message removeReblog(Message message, User user) {
        Validate.notNull(message);
        Validate.notNull(user);

        message.removeReblog(user);
        return saveOrUpdateMessage(message);
    }

    @Override
    public Boolean isMessageReblogged(Message message, User whoReblogged) {
        Validate.notNull(message);
        Validate.notNull(whoReblogged);

        return messageRepository.isMessageReblogged(message.getId(), whoReblogged);
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

        postMessage(reply);

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
}
