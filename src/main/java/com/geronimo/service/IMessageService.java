package com.geronimo.service;

import com.geronimo.model.Message;
import com.geronimo.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IMessageService {

    Page<Message> listFeedMessages(User currentUser, Pageable pageable);

    Page<Message> listUserMessagesAndReblogs(User author, Pageable pageable);

    Message saveOrUpdateMessage(Message message);

    Message postMessage(Message message);

    Message reblogMessage(Message messageToReblog, User whoReblogged);

    Message removeReblog(Message message, User user);

    Message likeMessage(Message message, User whoLiked);

    Message dislikeMessage(Message message, User whoDisliked);

    Boolean isMessageLiked(Message message, User whoLiked);

    Long countLikes(Message message);

    Boolean isMessageReblogged(Message message, User whoReblogged);

    Long countReblogs(Message message);

    void answerMessage(Message message, Message reply);

    Message getMessageById(Long id);

    void deleteMessage(Long id);

    void deleteMessage(Message message);
}
