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
@Table(name = "Portfolios")
public class PortfolioModel extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long portfolioId;

    private String title;

    private Timestamp completionDate;

    private String projectUrl;

    private String description;

    private Set<String> fileUrls;

    @ManyToMany
    @JoinTable(
            name = "portfolio_skill",
            joinColumns = @JoinColumn(name = "portfolioId"),
            inverseJoinColumns = @JoinColumn(name = "skillId")
    )
    private Set<SkillModel> skills;


    @OneToOne
    @JoinColumn(name = "projectId", nullable = false)
    private ProjectModel project;

    @ManyToOne
    @JoinColumn(name = "freelancerId", nullable = false)
    private FreelancerModel freelancer;

    }
