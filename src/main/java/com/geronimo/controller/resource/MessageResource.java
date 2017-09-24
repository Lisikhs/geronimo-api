package com.geronimo.controller.resource;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class MessageResource extends AbstractResource {
    private String text;
    private UserResource author;
}
