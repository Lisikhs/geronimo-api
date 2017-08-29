package com.geronimo.model;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.annotation.Version;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.*;

@Data
@Entity
@Table(name = "messages")
@EntityListeners(AuditingEntityListener.class)
public class Message {

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

    @Column(name = "date_created")
    @CreatedDate
    protected Date dateCreated;

    @Column(name = "last_updated")
    @LastModifiedDate
    protected Date lastUpdated;

    @Column(name = "version")
    @Version
    protected Long version;
}
