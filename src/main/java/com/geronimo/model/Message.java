package com.geronimo.model;

import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Setter;

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
public class Message extends AuditedEntity {

    @NotNull
    @ManyToOne
    private User author;

    @NotNull
    private String text;

    @ManyToMany(fetch = FetchType.EAGER)
    @Setter(value = AccessLevel.NONE)
    private Set<User> likes = new HashSet<>();

    @ManyToMany(fetch = FetchType.EAGER)
    @Setter(value = AccessLevel.NONE)
    private Set<User> reblogs = new HashSet<>();

    @OneToMany(fetch = FetchType.EAGER)
    @Setter(value = AccessLevel.NONE)
    private List<Message> answers = new LinkedList<>();

    public void addLike(User whoLiked) {
        likes.add(whoLiked);
    }

    public void removeLike(User whoLiked) {
        likes.remove(whoLiked);
    }

    public void addReblog(User whoReblogged) {
        reblogs.add(whoReblogged);
    }

    public void removeReblog(User whoReblogged) {
        reblogs.remove(whoReblogged);
    }

    public void addAnswer(Message answer) {
        answers.add(answer);
    }

    public void removeAnswer(Message answer) {
        answers.remove(answer);
    }


}
