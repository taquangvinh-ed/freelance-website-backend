package com.freelancemarketplace.backend.model;

import jakarta.persistence.*;

@Entity
public class ExperiencesModel extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long experience_id;


    @ManyToOne
    @JoinColumn(name = "location_id")
    private LocationsModel experience_location;

    //The experiences that belong to a freelancer
    @ManyToOne
    @JoinColumn(name = "freelancer_id")
    private FreelancersModel freelancerExperiences;
}
