package com.geronimo.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import org.hibernate.annotations.BatchSize;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

@Data
@EqualsAndHashCode(callSuper = true, exclude = {"likes", "reblogs", "answers"})
@Entity
@Table(name = "messages")
@ToString(exclude = {"author", "reblogs"}, callSuper = true)
@NoArgsConstructor
@JsonIgnoreProperties(value = { "answers" })
public class Message extends AuditedEntity {

    public Message(String text, User author) {
        this.text = text;
        this.author = author;
    }

    @NotNull
    @ManyToOne
    private User author;

    @NotNull
    private String text;

    @ManyToMany
    @BatchSize(size = 20)
    private Set<User> likes = new HashSet<>();

    @ManyToMany
    @JoinTable(joinColumns = @JoinColumn(name = "message_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id"))
    @BatchSize(size = 20)
    private Set<User> reblogs = new HashSet<>();

    // why CascadeType.REMOVE? because we want to remove all the answers if the original message is removed
    @OneToMany(cascade = {CascadeType.REMOVE})
    @BatchSize(size = 20)
    private List<Message> answers = new LinkedList<>();

    public void addLike(User user) {
        likes.add(user);
    }

    public void removeLike(User user) {
        likes.remove(user);
    }

    public void addReblog(User user) {
        reblogs.add(user);
    }

    public void removeReblog(User user) {
        reblogs.remove(user);
    }

    public void addAnswer(Message answer) {
        answers.add(answer);
    }
}
