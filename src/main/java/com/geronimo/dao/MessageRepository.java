package com.geronimo.dao;

import com.geronimo.model.Message;
import com.geronimo.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface MessageRepository extends PagingAndSortingRepository<Message, Long> {

    Message findByText(String text);

    @Query("select count(l) from Message m join m.likes as l where m.id=:messageId")
    Long countLikes(@Param("messageId") Long messageId);

    @Query("select count(r) from Message m join m.reblogs r where m.id=:messageId")
    Long countReblogs(@Param("messageId") Long messageId);

    Page<Message> findByAuthorInOrderByDateCreatedDesc(List<User> followingUsers, Pageable pageable);


}
