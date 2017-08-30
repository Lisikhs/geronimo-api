package com.geronimo.dao;

import com.geronimo.model.Message;
import org.springframework.data.jpa.repository.JpaRepository;


public interface MessageRepository extends JpaRepository<Message, Long> {

    Message findByText(String text);
}
