package com.freelancemarketplace.backend.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.io.Serializable;
import java.sql.Timestamp;
import java.time.LocalDate;

public class Bio implements Serializable {

    private LocalDate date_of_birth;

    private String gender;

    private String nationality;

    private String summary;

    private String twitter_link;

    private String linkedin_link;

    private String facebook_link;

}
