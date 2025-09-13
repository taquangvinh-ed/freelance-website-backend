package com.freelancemarketplace.backend.model;

import jakarta.persistence.*;

import java.util.List;

@Entity
public class FreelancersModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long freelancer_id;

    @OneToMany(mappedBy = "freelancer")
    private List<VideosModel> videos;

    @OneToOne(mappedBy = "freelancer")
    private PortfoliosModel portfolio;
}
