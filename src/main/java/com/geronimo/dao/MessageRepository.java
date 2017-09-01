package com.geronimo.dao;

import com.geronimo.model.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface MessageRepository extends JpaRepository<Message, Long> {

    Message findByText(String text);

    @Query("select count(l) from Message m join m.likes as l where m.id=:messageId")
    Long countLikes(@Param("messageId") Long messageId);

    @Query("select count(r) from Message m join m.reblogs r where m.id=:messageId")
    Long countReblogs(@Param("messageId") Long messageId);
}
