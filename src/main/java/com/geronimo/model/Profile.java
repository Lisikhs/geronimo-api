package com.geronimo.model;


import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;
import javax.persistence.Lob;
import java.util.Date;

@Data
@Embeddable
@NoArgsConstructor
public class Profile {

    public Profile(String status, Date dateOfBirth) {
        this.status = status;
        this.dateOfBirth = dateOfBirth;
    }

    private String status;

    @Lob
    private byte[] picture;

    private Date dateOfBirth;
}
