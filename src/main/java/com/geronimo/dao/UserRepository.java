package com.geronimo.dao;

import com.geronimo.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface UserRepository extends JpaRepository<User, Long> {

    User findByUsername(String username);

    @Query("select u from User u join u.followers as f where f.id = :followerId")
    List<User> getFollowingUsers(@Param("followerId") Long followerId);

    void deleteByUsername(String username);
}
