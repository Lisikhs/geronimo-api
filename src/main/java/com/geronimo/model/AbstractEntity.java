package com.geronimo.model;

import lombok.Setter;

import javax.persistence.MappedSuperclass;

@Setter
@MappedSuperclass
public class AbstractEntity {

    @javax.persistence.Version
    protected Long version;
}
