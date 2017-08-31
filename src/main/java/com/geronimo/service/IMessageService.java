package com.geronimo.service;

import com.geronimo.model.Message;
import com.geronimo.model.User;
import org.springframework.transaction.annotation.Transactional;

public interface IMessageService {

    void saveOrUpdateMessage(Message message);

    void postMessage(Message message);

    void reblogMessage(Message messageToReblog, User whoReblogged);

    void likeMessage(Message message, User whoLiked);

    Long countLikes(Message message);

    Long countReblogs(Message message);

    void answerMessage(Message message, Message reply);

    Message getMessageById(Long id);

    void deleteMessage(Long id);

    @Transactional
    void deleteMessage(Message message);
}
