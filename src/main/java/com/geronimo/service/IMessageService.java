package com.geronimo.service;

import com.geronimo.exceptions.RebloggingOwnMessageException;
import com.geronimo.model.Message;
import com.geronimo.model.User;

public interface IMessageService {

    void saveOrUpdateMessage(Message message);

    void postMessage(Message message);

    void reblogMessage(Message messageToReblog, User whoReblogged) throws RebloggingOwnMessageException;

    void likeMessage(Message message, User whoLiked);

    Long countLikes(Message message);

    Long countReblogs(Message message);

    void answerMessage(Message message, Message reply);

    Message getMessageById(Long id);

    void deleteMessage(Long id);

    void deleteMessage(Message message);
}
