package com.freelancemarketplace.backend.model;

import jakarta.persistence.*;

@Entity
public class EducationsModel extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long education_id;

    @ManyToOne
    @JoinColumn(name = "freelancer_id")
    private FreelancersModel freelancerEducations;
}
