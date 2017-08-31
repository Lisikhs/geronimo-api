package com.geronimo.service;

import com.geronimo.model.Message;
import com.geronimo.model.User;

public interface IMessageService {

    void saveOrUpdateMessage(Message message);

    void postMessage(Message message);

    void reblogMessage(Message messageToReblog, User whoReblogged);

    void answerMessage(Message message, Message reply);

    Message getMessageById(Long id);

    void deleteMessage(Long id);
}
