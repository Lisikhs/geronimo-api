package com.geronimo.controller.resource;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.springframework.hateoas.ResourceSupport;

@JsonPropertyOrder({"id"})
public abstract class AbstractResource extends ResourceSupport {

    @JsonProperty("id")
    private Long id;

    // this is a workaround, we need it because
    // getId() method is already defined with a different return type in ResourceSupport class
    @JsonIgnore
    public Long getIdentifier() {
        return id;
    }

    public void setIdentifier(Long identifier) {
        this.id = identifier;
    }
}
