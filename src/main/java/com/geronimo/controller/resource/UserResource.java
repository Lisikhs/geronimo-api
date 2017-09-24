package com.geronimo.controller.resource;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.xml.bind.annotation.XmlRootElement;


@Data
@EqualsAndHashCode(callSuper = true)
@XmlRootElement(name = "user")
public class UserResource extends AbstractResource {
    private String username;
    private String password;
}
