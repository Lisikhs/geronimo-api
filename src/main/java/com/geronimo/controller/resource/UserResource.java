package com.geronimo.controller.resource;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class UserResource extends AbstractResource {
    private String username;
    private String password;
}
