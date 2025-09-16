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
@Table(name = "Skils")
public class SkillModel extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long skillId;

    private String name;
    private String description;

    @ManyToMany(mappedBy = "skills")
    private Set<PortfolioModel> portfolios;

    @ManyToMany(mappedBy = "skills")
    private Set<CategoryModel> categories;

    //Freelancers that use this skill
    @ManyToMany(mappedBy = "skills")
    private Set<FreelancerModel> freelancers;

    @ManyToMany(mappedBy = "skills")
    private Set<TestModel> testsList;

    //Projects that require this skill
    @ManyToMany(mappedBy = "skills")
    private Set<ProjectModel> projectsList;

    //Products that require this skill
    @ManyToMany(mappedBy = "skills" )
    private Set<ProductModel> productsList;

    //Teams that have this skill
    @ManyToMany(mappedBy = "skills" )
    private Set<TeamModel> teams;

}
