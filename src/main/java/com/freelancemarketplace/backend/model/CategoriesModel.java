package com.freelancemarketplace.backend.model;

import jakarta.persistence.*;

import java.util.Set;

@Entity
public class CategoriesModel extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long category_id;

    private String category_name;

    private String category_image;


    @ManyToMany
    @JoinTable(
            name = "category_skill",
            joinColumns = @JoinColumn(name="category_id"),
            inverseJoinColumns = @JoinColumn(name="skill_id")
    )
    private Set<SkillsModel> skills;

    @OneToMany(mappedBy = "category")
    private Set<ProjectsModel> projects;

    public CategoriesModel() {
    }


    public Long getCategory_id() {
        return category_id;
    }

    public void setCategory_id(Long category_id) {
        this.category_id = category_id;
    }

    public String getCategory_name() {
        return category_name;
    }

    public void setCategory_name(String category_name) {
        this.category_name = category_name;
    }

    public String getCategory_image() {
        return category_image;
    }

    public void setCategory_image(String category_image) {
        this.category_image = category_image;
    }

    public Set<SkillsModel> getSkills() {
        return skills;
    }

    public void setSkills(Set<SkillsModel> skills) {
        this.skills = skills;
    }

    public Set<ProjectsModel> getProjects() {
        return projects;
    }

    public void setProjects(Set<ProjectsModel> projects) {
        this.projects = projects;
    }
}
