package com.geronimo.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.BatchSize;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Data
@ToString(callSuper = true, exclude = {"messages", "followers", "password", "roles", "permissions"})
@EqualsAndHashCode(callSuper = true, exclude = {"messages", "followers", "roles", "permissions"})
@Table(name = "users")
@NoArgsConstructor
@JsonIgnoreProperties(value = { "messages", "followers", "password", "roles", "permissions" })
public class User extends AuditedEntity {

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    @NotNull
    private String username;

    @NotNull
    private String password;

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @ManyToMany
    @JoinTable(name = "users_roles", joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();

    @ManyToMany
    @JoinTable(name = "users_permissions", joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "permission_id"))
    private Set<Permission> permissions;

    private boolean accountNonExpired = true;
    private boolean accountNonLocked = true;
    private boolean credentialsNonExpired = true;
    private boolean enabled = true;
    private LocalDate passwordUpdated;

    @Embedded
    private Profile profile;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "author")
    @BatchSize(size = 20)
    private List<Message> messages = new ArrayList<>();

    @ManyToMany
    @JoinTable(name = "followers_following",
            joinColumns = {@JoinColumn(name = "following_id")},
            inverseJoinColumns = {@JoinColumn(name = "follower_id")})
    @BatchSize(size = 20)
    private Set<User> followers = new HashSet<>();

    public void addMessage(Message message) {
        if (message != null) {
            messages.add(message);
        }
    }

    public void addFollower(User follower) {
        if (follower != null) {
            followers.add(follower);
        }
    }

    public void removeFollower(User toUnfollow) {
        if (toUnfollow != null) {
            followers.remove(toUnfollow);
        }
    }
}
