package com.freelancemarketplace.backend.model;

import java.io.Serializable;
import java.time.LocalDate;

public class Bio implements Serializable {

    private LocalDate dateOfBirth;

    private String gender;

    private String nationality;

    private String summary;

    private String twitterLink;

    private String linkedinLink;

    private String facebookLink;

}
