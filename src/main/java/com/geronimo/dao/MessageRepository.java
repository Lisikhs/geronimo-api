package com.geronimo.dao;

import com.geronimo.model.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


public interface MessageRepository extends JpaRepository<Message, Long> {

    Message findByText(String text);

    @Query(value = "select count(*) from messages_likes where message_id=?1", nativeQuery = true)
    Long countLikes(Long messageId);

    @Query(value = "select count(*) from messages_reblogs where message_id=?1", nativeQuery = true)
    Long countReblogs(Long messageId);
}
