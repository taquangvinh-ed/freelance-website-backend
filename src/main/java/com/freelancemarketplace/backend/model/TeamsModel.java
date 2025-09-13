package com.freelancemarketplace.backend.model;

import jakarta.persistence.*;

import java.util.List;

@Entity
public class TeamsModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long team_id;

    @OneToMany(mappedBy = "team")
    private List<VideosModel> videos;

    @ManyToMany
    @JoinTable(
        name = "team_freelancers",
        joinColumns = @JoinColumn(name = "team_id"),
        inverseJoinColumns = @JoinColumn(name = "freelancer_id")
    )
    private List<FreelancersModel> teamFreelancers;

}
