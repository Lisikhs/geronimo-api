package com.geronimo.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true, exclude = {"roles", "users"})
@Table(name = "permissions")
public class Permission extends AuditedEntity {

    public Permission(String name) {
        this.name = name;
    }

    private String name;

    @ManyToMany(mappedBy = "permissions")
    private Set<Role> roles = new HashSet<>();

    @ManyToMany(mappedBy = "permissions")
    private Set<User> users = new HashSet<>();
}
