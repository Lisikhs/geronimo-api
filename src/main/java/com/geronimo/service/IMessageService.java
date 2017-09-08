package com.geronimo.service;

import com.geronimo.model.Message;
import com.geronimo.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IMessageService {

    Page<Message> listFeedMessages(User currentUser, Pageable pageable);

    Page<Message> listUserMessagesAndReblogs(User author, Pageable pageable);

    void saveOrUpdateMessage(Message message);

    void postMessage(Message message);

    void reblogMessage(Message messageToReblog, User whoReblogged);

    void likeMessage(Message message, User whoLiked);

    Long countLikes(Message message);

    Long countReblogs(Message message);

    void answerMessage(Message message, Message reply);

    Message getMessageById(Long id);

    void deleteMessage(Long id);

    void deleteMessage(Message message);
}
