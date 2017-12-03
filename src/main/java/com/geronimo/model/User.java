package com.geronimo.model;

import lombok.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import org.hibernate.annotations.BatchSize;
import org.hibernate.secure.spi.GrantedPermission;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

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
