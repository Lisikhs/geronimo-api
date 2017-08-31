package com.geronimo.model;

import lombok.*;
import org.hibernate.annotations.BatchSize;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.*;

@Entity
@Data
@ToString(exclude = {"following", "messages", "followers"}, callSuper = true)
@EqualsAndHashCode(callSuper = true, exclude = {"messages", "following", "followers"})
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

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(name = "followers_following",
            joinColumns = {@JoinColumn(name = "follower_id")},
            inverseJoinColumns = {@JoinColumn(name = "following_id")})
    @BatchSize(size = 20)
    private Set<User> following = new HashSet<>();

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})

    @JoinTable(name = "followers_following",
            joinColumns = {@JoinColumn(name = "following_id")},
            inverseJoinColumns = {@JoinColumn(name = "follower_id")})
    @BatchSize(size = 20)
    private Set<User> followers = new HashSet<>();

    private void addFollower(User follower) {
        followers.add(follower);
    }

    private void removeFollower(User follower) {
        followers.remove(follower);
    }

    public void addMessage(Message message) {
        if (message != null) {
            messages.add(message);
        }
    }

    public void follow(User toFollow) {
        if (toFollow != null) {
            toFollow.addFollower(this);
            following.add(toFollow);
        }
    }

    public void unfollow(User toUnfollow) {
        if (toUnfollow != null) {
            toUnfollow.removeFollower(this);
            following.remove(toUnfollow);
        }
    }
}
