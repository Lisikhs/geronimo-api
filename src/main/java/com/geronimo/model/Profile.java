package com.geronimo.model;


import lombok.Data;
import lombok.ToString;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@ToString(callSuper = true)
public class Profile extends AuditedEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String status;

    @Lob
    private byte[] picture;

    private Date dateOfBirth;

}
