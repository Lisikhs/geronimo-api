package com.geronimo.model;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

@Data
@Entity
@Table(name = "messages")
public class Message extends AuditedEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    private Long userId;

    @ManyToMany
    private Set<User> likes = new HashSet<>();

    @ManyToMany
    private Set<User> reblogs = new HashSet<>();

    @OneToMany
    private List<Message> answers = new LinkedList<>();
}
