package com.geronimo.dao;

import com.geronimo.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface UserRepository extends JpaRepository<User, Long> {

    User findByUsername(String username);

    User findByUsernameAndPassword(String username, String password);

    void deleteByUsername(String username);

    @Query("select u from User u join u.followers as f where f.id = :followerId")
    List<User> getFollowingUsers(@Param("followerId") Long followerId);

    @Query("SELECT (COUNT(u) > 0) FROM User u WHERE u.id = :userId AND :anotherUser member of u.followers") //SELECT (count(u) > 0) from User u join u.followers f where u.id = :anotherUserId AND f.id = :userId
    Boolean isUserSubscribedToAnotherUser(@Param("userId") Long userId, @Param("anotherUser") User anotherUser);

    @Query("SELECT u.messages.size FROM User u WHERE u.id = :userId")
    Integer getCountOfMessages(@Param("userId") Long userId);

    @Query("SELECT u.followers.size FROM User u WHERE u.id = :userId")
    Integer getCountOfFollowedUsers(@Param("userId") Long userId);

    @Query("SELECT COUNT(u) FROM User u join u.followers f WHERE f.id = :userId")
    Integer getCountOfFollowingUsers(@Param("userId") Long userId);

}
