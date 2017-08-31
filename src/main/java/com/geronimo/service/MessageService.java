package com.geronimo.service;

import com.geronimo.dao.MessageRepository;
import com.geronimo.model.Message;
import com.geronimo.model.User;
import org.apache.commons.lang3.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional(readOnly = true)
public class MessageService implements IMessageService {

    private MessageRepository messageRepository;

    @Transactional
    @Override
    public void saveOrUpdateMessage(Message message) {
        messageRepository.save(message);
    }

    @Transactional
    @Override
    public void reblogMessage(Message messageToReblog, User whoReblogged) {
        Validate.notNull(messageToReblog);
        Validate.notNull(whoReblogged);

        messageToReblog.addReblog(whoReblogged);
        saveOrUpdateMessage(messageToReblog);
    }

    @Transactional
    @Override
    public void postMessage(Message message) {
        saveOrUpdateMessage(message);
    }

    @Transactional
    @Override
    public void likeMessage(Message message, User whoLiked) {
        message.addLike(whoLiked);
        saveOrUpdateMessage(message);
    }

    @Override
    public Long countLikes(Message message) {
        return messageRepository.countLikes(message.getId());
    }

    @Override
    public Long countReblogs(Message message) {
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
        return messageRepository.findOne(id);
    }

    @Transactional
    @Override
    public void deleteMessage(Long id) {
        messageRepository.delete(id);
    }

    @Transactional
    @Override
    public void deleteMessage(Message message) {
        messageRepository.delete(message);
    }

    @Autowired
    public void setMessageRepository(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }
}
