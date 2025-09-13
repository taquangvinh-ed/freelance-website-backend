package com.freelancemarketplace.backend.model;

import jakarta.persistence.*;

import java.util.List;
import java.util.Set;

@Entity
public class FreelancersModel extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long freelancer_id;

    @OneToMany(mappedBy = "freelancer")
    private List<VideosModel> videos;

    @OneToOne(mappedBy = "freelancer")
    private PortfoliosModel portfolio;

    @ManyToMany
    @JoinTable(
            name = "frelancer_languages",
            joinColumns = @JoinColumn(name = "freelancer_id"),
            inverseJoinColumns = @JoinColumn(name = "language_id")
    )
    private Set <LanguagesModel> freelancer_languages;

    @OneToOne
    @JoinColumn(name = "analytics_id")
    private AnalyticsModel freelancer_analytics;
}
