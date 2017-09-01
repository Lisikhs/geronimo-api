package com.geronimo.model;

import lombok.*;
import org.hibernate.annotations.BatchSize;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.*;

@Entity
@Data
@ToString(callSuper = true, exclude = {"messages", "followers"})
@EqualsAndHashCode(callSuper = true, exclude = {"messages", "followers"})
@Table(name = "users")
@NoArgsConstructor
public class User extends AuditedEntity {

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    @NotNull
    private String username;

    @NotNull
    private String password;

    @Embedded
    private Profile profile;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "author")
    @BatchSize(size = 20)
    private List<Message> messages = new ArrayList<>();

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE})
    @JoinTable(name = "followers_following",
            joinColumns = {@JoinColumn(name = "follower_id")},
            inverseJoinColumns = {@JoinColumn(name = "following_id")})
    @BatchSize(size = 20)
    private Set<User> followers = new HashSet<>();

    public void addMessage(Message message) {
        if (message != null) {
            messages.add(message);
        }
    }

    public void addFollower(User toFollow) {
        if (toFollow != null) {
            followers.add(toFollow);
        }
    }

    public void removeFollower(User toUnfollow) {
        if (toUnfollow != null) {
            followers.remove(toUnfollow);
        }
    }
}
