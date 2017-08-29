package com.geronimo.model;


import lombok.Data;

import javax.persistence.Embeddable;
import javax.persistence.Lob;
import java.util.Date;

@Data
@Embeddable
public class Profile {

    public Profile(String status, Date dateOfBirth) {
        this.status = status;
        this.dateOfBirth = dateOfBirth;
    }

    public Profile() {
    }

    private String status;

    @Lob
    private byte[] picture;

    private Date dateOfBirth;

}
