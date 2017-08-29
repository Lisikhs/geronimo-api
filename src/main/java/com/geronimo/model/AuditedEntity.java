package com.geronimo.model;

import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.util.Date;

@Setter
@ToString
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
abstract class AuditedEntity {

    @CreatedDate
    protected Date dateCreated;

    @LastModifiedDate
    protected Date lastUpdated;

    @javax.persistence.Version
    protected Long version;
}
