package com.geronimo.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity(name = "users")
@Data
public class User {
    @Id
    private Long id;
    private String username;
    private String password;
}
