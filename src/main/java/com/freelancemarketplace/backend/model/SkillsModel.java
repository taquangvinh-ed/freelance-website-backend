package com.freelancemarketplace.backend.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class SkillsModel extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long skill_id;

    private String name;
    private String description;

    @ManyToMany(mappedBy = "skills")
    private Set<PortfoliosModel> portfolios;

    @ManyToMany(mappedBy = "skills")
    private Set<CategoriesModel> categories;

    //Freelancers that use this skill
    @ManyToMany(mappedBy = "skills")
    private Set<FreelancersModel> freelancers;

    @ManyToMany(mappedBy = "skillsTests")
    private Set<TestsModel> testsList;

    //Projects that require this skill
    @ManyToMany(mappedBy = "skills")
    private Set<ProjectsModel> projectsList;

    //Products that require this skill
    @ManyToMany(mappedBy = "skills" )
    private Set<ProductsModel> productsList;

    //Teams that have this skill
    @ManyToMany(mappedBy = "skills" )
    private Set<TeamsModel> teams;

}
