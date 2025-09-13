package com.freelancemarketplace.backend.model;

import jakarta.persistence.*;

import java.util.Set;

@Entity
public class SkillsModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long skill_id;

    @ManyToMany(mappedBy = "skills")
    private Set<PortfoliosModel> portfolios;

    @ManyToMany(mappedBy = "skills")
    private Set<Categories> categories;

    //Freelancers that use this skill
    @ManyToMany(mappedBy = "skills")
    private Set<FreelancersModel> freelancers;

    @ManyToMany(mappedBy = "skillsTests")
    private Set<TestsModel> testsList;
}
