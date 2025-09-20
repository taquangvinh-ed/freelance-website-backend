package com.freelancemarketplace.backend.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
public class Bio implements Serializable {

    private LocalDate dateOfBirth;

    private String gender;

    private String nationality;

    private String summary;

    private String twitterLink;

    private String linkedinLink;

    private String facebookLink;

}
