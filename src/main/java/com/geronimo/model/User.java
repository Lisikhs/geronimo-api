package com.geronimo.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.*;

@Entity
@Data
@ToString(exclude = {"following", "messages", "followers"}, callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Table(name = "users")
public class User extends AuditedEntity {

    @NotNull
    private String username;

    @NotNull
    private String password;

    @Embedded
    private Profile profile = new Profile();

    @OneToMany(cascade = CascadeType.ALL)
    private List<Message> messages = new ArrayList<>();

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private Set<User> following = new HashSet<>();

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private Set<User> followers = new HashSet<>();

}
