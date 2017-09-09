package com.geronimo.dao;

import com.geronimo.model.Message;
import com.geronimo.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface MessageRepository extends JpaRepository<Message, Long> {

    @Query("select count(l) from Message m join m.likes as l where m.id=:messageId")
    Long countLikes(@Param("messageId") Long messageId);

    @Query("select count(r) from Message m join m.reblogs r where m.id=:messageId")
    Long countReblogs(@Param("messageId") Long messageId);

    // TODO: change this query as it might hit limit of SQL's IN function on following users
    Page<Message> findByAuthorIn(List<User> followingUsers, Pageable pageable);

    @Query("select m from Message m where m.author = :user or :user member of m.reblogs")
    Page<Message> findUserMessages(@Param("user") User author, Pageable pageable);
}
