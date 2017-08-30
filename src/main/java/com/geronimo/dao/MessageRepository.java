package com.geronimo.dao;

import com.geronimo.model.Message;
import org.springframework.data.repository.CrudRepository;

public interface MessageRepository extends CrudRepository<Message, Long> {

    Message findByText(String text);
}
