package com.geronimo.controller.resource;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.xml.bind.annotation.XmlRootElement;


@Data
@EqualsAndHashCode(callSuper = true)
@XmlRootElement(name = "message")
public class MessageResource extends AbstractResource {
    private String text;
    private UserResource author;
}
