package com.freelancemarketplace.backend.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class PortfoliosModel extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long portfolio_id;

    private String project_titel;

    private Timestamp completion_date;

    private String project_url;

    private String description;

    private Set<String> file_urls;

    @ManyToMany
    @JoinTable(
            name = "portfolio_skill",
            joinColumns = @JoinColumn(name = "portfolio_id"),
            inverseJoinColumns = @JoinColumn(name = "skill_id")
    )
    private Set<SkillsModel> skills;


    @OneToOne
    @JoinColumn(name = "project_id", nullable = false)
    private ProjectsModel project;

    @ManyToOne
    @JoinColumn(name = "freelancer_id", nullable = false)
    private FreelancersModel freelancer;

    }
