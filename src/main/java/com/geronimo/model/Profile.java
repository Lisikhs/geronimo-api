package com.geronimo.model;


import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@ToString(callSuper = true)
@EntityListeners(AuditingEntityListener.class)
public class Profile {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String status;

    @Lob
    private byte[] picture;

    private Date dateOfBirth;

    @Column(name = "date_created")
    @CreatedDate
    protected Date dateCreated;

    @Column(name = "last_updated")
    @LastModifiedDate
    protected Date lastUpdated;

    @Column(name = "version")
    @Version
    protected Long version;

}
