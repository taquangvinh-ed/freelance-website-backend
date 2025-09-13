package com.freelancemarketplace.backend.model;

import jakarta.persistence.*;

import java.sql.Timestamp;
import java.util.Set;

@Entity
public class PortfoliosModel extends BaseEntity{
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
    private Set<SkillsModel> skills;

    @ManyToOne
    @JoinColumn(name = "freelancer_id", nullable = false)
    private FreelancersModel freelancer;

    public PortfoliosModel(Long portfolio_id, String project_titel, Timestamp completion_date, String project_url, String description, Set<String> file_urls, ProjectsModel project, Set<SkillsModel> skills, FreelancersModel freelancer) {
        this.portfolio_id = portfolio_id;
        this.project_titel = project_titel;
        this.completion_date = completion_date;
        this.project_url = project_url;
        this.description = description;
        this.file_urls = file_urls;
        this.project = project;
        this.skills = skills;
        this.freelancer = freelancer;
    }

    public Long getPortfolio_id() {
        return portfolio_id;
    }

    public void setPortfolio_id(Long portfolio_id) {
        this.portfolio_id = portfolio_id;
    }

    public String getProject_titel() {
        return project_titel;
    }

    public void setProject_titel(String project_titel) {
        this.project_titel = project_titel;
    }

    public Timestamp getCompletion_date() {
        return completion_date;
    }

    public void setCompletion_date(Timestamp completion_date) {
        this.completion_date = completion_date;
    }

    public String getProject_url() {
        return project_url;
    }

    public void setProject_url(String project_url) {
        this.project_url = project_url;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<String> getFile_urls() {
        return file_urls;
    }

    public void setFile_urls(Set<String> file_urls) {
        this.file_urls = file_urls;
    }

    public ProjectsModel getProject() {
        return project;
    }

    public void setProject(ProjectsModel project) {
        this.project = project;
    }

    public Set<SkillsModel> getSkills() {
        return skills;
    }

    public void setSkills(Set<SkillsModel> skills) {
        this.skills = skills;
    }

    public FreelancersModel getFreelancer() {
        return freelancer;
    }

    public void setFreelancer(FreelancersModel freelancer) {
        this.freelancer = freelancer;
    }
}
