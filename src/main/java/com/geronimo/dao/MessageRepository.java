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

    @Query("select m.likes.size from Message m where m.id=:messageId")
    Long countLikes(@Param("messageId") Long messageId);

    @Query("select m.reblogs.size from Message m where m.id=:messageId")
    Long countReblogs(@Param("messageId") Long messageId);

    // TODO: change this query as it might hit limit of SQL's IN function on following users
    Page<Message> findByAuthorIn(List<User> followingUsers, Pageable pageable);

    @Query("select m from Message m where m.author = :user or :user member of m.reblogs")
    Page<Message> findUserMessages(@Param("user") User author, Pageable pageable);

    @Query("SELECT (COUNT(m) = 1) FROM Message m WHERE m.id = :messageId AND :user member of m.likes")
    Boolean isMessageLiked(@Param("messageId") Long messageId, @Param("user") User user);

    @Query("SELECT (COUNT(m) = 1) FROM Message m WHERE m.id = :messageId AND :user member of m.reblogs")
    Boolean isMessageReblogged(@Param("messageId") Long messageId, @Param("user") User whoReblogged);
}
