package com.freelancemarketplace.backend.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class ExperiencesModel extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long experience_id;

    private String title;
    private String organization;
    private String start_date;
    private String end_date;
    private String description;


    @ManyToOne
    @JoinColumn(name = "location_id")
    private LocationsModel experience_location;

    //The experiences that belong to a freelancer
    @ManyToOne
    @JoinColumn(name = "freelancer_id")
    private FreelancersModel freelancerExperiences;

}
