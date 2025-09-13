package com.freelancemarketplace.backend.model;

import jakarta.persistence.*;

@Entity
public class Budgets extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long budget_id;

    private String budget_type;

    private Long budget_value;

    @OneToOne(mappedBy = "budget")
    private ProjectsModel project;

    public Budgets(Long budget_id, String budget_type, Long budget_value, ProjectsModel project) {
        this.budget_id = budget_id;
        this.budget_type = budget_type;
        this.budget_value = budget_value;
        this.project = project;
    }

    public Long getBudget_id() {
        return budget_id;
    }

    public void setBudget_id(Long budget_id) {
        this.budget_id = budget_id;
    }

    public String getBudget_type() {
        return budget_type;
    }

    public void setBudget_type(String budget_type) {
        this.budget_type = budget_type;
    }

    public Long getBudget_value() {
        return budget_value;
    }

    public void setBudget_value(Long budget_value) {
        this.budget_value = budget_value;
    }

    public ProjectsModel getProject() {
        return project;
    }

    public void setProject(ProjectsModel project) {
        this.project = project;
    }
}
