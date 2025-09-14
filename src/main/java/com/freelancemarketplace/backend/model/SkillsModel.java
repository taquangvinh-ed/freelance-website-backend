package com.freelancemarketplace.backend.model;

import jakarta.persistence.*;

import java.util.Set;

@Entity
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

    public SkillsModel(Long skill_id, String name, String description, Set<PortfoliosModel> portfolios, Set<CategoriesModel> categories, Set<FreelancersModel> freelancers, Set<TestsModel> testsList, Set<ProjectsModel> projectsList, Set<ProductsModel> productsList, Set<TeamsModel> teams) {
        this.skill_id = skill_id;
        this.name = name;
        this.description = description;
        this.portfolios = portfolios;
        this.categories = categories;
        this.freelancers = freelancers;
        this.testsList = testsList;
        this.projectsList = projectsList;
        this.productsList = productsList;
        this.teams = teams;
    }

    public Long getSkill_id() {
        return skill_id;
    }

    public void setSkill_id(Long skill_id) {
        this.skill_id = skill_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<PortfoliosModel> getPortfolios() {
        return portfolios;
    }

    public void setPortfolios(Set<PortfoliosModel> portfolios) {
        this.portfolios = portfolios;
    }

    public Set<CategoriesModel> getCategories() {
        return categories;
    }

    public void setCategories(Set<CategoriesModel> categories) {
        this.categories = categories;
    }

    public Set<FreelancersModel> getFreelancers() {
        return freelancers;
    }

    public void setFreelancers(Set<FreelancersModel> freelancers) {
        this.freelancers = freelancers;
    }

    public Set<TestsModel> getTestsList() {
        return testsList;
    }

    public void setTestsList(Set<TestsModel> testsList) {
        this.testsList = testsList;
    }

    public Set<ProjectsModel> getProjectsList() {
        return projectsList;
    }

    public void setProjectsList(Set<ProjectsModel> projectsList) {
        this.projectsList = projectsList;
    }

    public Set<ProductsModel> getProductsList() {
        return productsList;
    }

    public void setProductsList(Set<ProductsModel> productsList) {
        this.productsList = productsList;
    }

    public Set<TeamsModel> getTeams() {
        return teams;
    }

    public void setTeams(Set<TeamsModel> teams) {
        this.teams = teams;
    }
}
