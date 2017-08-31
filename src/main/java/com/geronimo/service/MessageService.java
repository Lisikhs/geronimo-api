package com.geronimo.service;

import com.geronimo.dao.MessageRepository;
import com.geronimo.model.Message;
import com.geronimo.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(readOnly = true)
public class MessageService implements IMessageService {

    private MessageRepository messageRepository;

    @Override
    @Transactional
    public void saveOrUpdateMessage(Message message) {
        messageRepository.save(message);
    }

    @Override
    public void postMessage(Message message, User whoPosted) {
        if (message != null && whoPosted != null) {
            message.setAuthor(whoPosted);
            saveOrUpdateMessage(message);

            //TODO: Figure out how to save the message in user's list of messages

        }
    }

    @Override
    public void reblogMessage(Message messageToReblog, User whoReblogged) {
        if (messageToReblog != null && whoReblogged != null) {
            //TODO: Should reblogged message be stored in user's list of messages?

            messageToReblog.addReblog(whoReblogged);
            saveOrUpdateMessage(messageToReblog);
        }
    }

    @Override
    public void answerMessage(Message message, Message reply, User whoReplied) {
        if (message != null && reply != null && whoReplied != null) {
            reply.setAuthor(whoReplied);
            saveOrUpdateMessage(reply);

            message.addAnswer(reply);
            saveOrUpdateMessage(message);
        }
    }

    @Override
    public Message getMessageById(Long id) {
        return messageRepository.findOne(id);
    }

    @Override
    public void deleteMessage(Long id) {
        messageRepository.delete(id);
    }

    @Autowired
    public void setMessageRepository(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }
}
