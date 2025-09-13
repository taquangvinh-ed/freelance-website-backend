package com.freelancemarketplace.backend.model;

import jakarta.persistence.*;

import java.sql.Timestamp;
import java.util.Set;

@Entity
public class PortfoliosModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long portfolio_id;

    private String project_titel;

    private Timestamp completion_date;

    private String project_url;

    private String description;

    private Set<String> file_urls;

    @ManyToOne
    @JoinTable(
            name = "portfolio_skill",
            joinColumns = @JoinColumn(name = "portfolio_id"),
            inverseJoinColumns = @JoinColumn(name = "skill_id")
    )

    @OneToOne
    @JoinColumn(name = "project_id", nullable = false)
    private ProjectsModel project;
    private Set<Skills> skills;

    @ManyToOne
    @JoinColumn(name = "freelancer_id", nullable = false)
    private FreelancersModel freelancer;
}
