package com.freelancemarketplace.backend.skill.domain.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;
import com.freelancemarketplace.backend.audit.domain.model.BaseEntity;
import com.freelancemarketplace.backend.category.domain.model.CategoryModel;
import com.freelancemarketplace.backend.freelancer.domain.model.FreelancerModel;
import com.freelancemarketplace.backend.freelancer.domain.model.PortfolioModel;
import com.freelancemarketplace.backend.product.domain.model.ProductModel;
import com.freelancemarketplace.backend.project.domain.model.ProjectModel;
import com.freelancemarketplace.backend.team.domain.model.TeamModel;
import com.freelancemarketplace.backend.test.domain.model.TestModel;

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
    private Set<CategoryModel> categories = new HashSet<>();

    //Freelancers that use this skill
    @ManyToMany(mappedBy = "skills")
    private Set<FreelancerModel> freelancers = new HashSet<>();

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
