package com.geronimo.model;

import lombok.Setter;
import lombok.ToString;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.MappedSuperclass;
import javax.persistence.SequenceGenerator;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Setter
@ToString
@MappedSuperclass
abstract class AuditedEntity {

    @NotNull
    protected Date date_created;

    @NotNull
    protected Date last_updated;

    protected Long version = 1L;
}
