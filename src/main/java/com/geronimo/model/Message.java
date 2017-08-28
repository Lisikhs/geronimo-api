package com.geronimo.model;

import lombok.Data;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.LinkedList;
import java.util.List;

@Data
@Entity
@Table(name = "messages")
public class Message extends AuditedEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    private Long user_id;

    @NotNull
    private Long count_of_reblogs;

    @NotNull
    private Long count_of_likes;

    @ManyToMany
    private List<Message> answers = new LinkedList<>();
}
