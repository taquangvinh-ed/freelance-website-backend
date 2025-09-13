package com.freelancemarketplace.backend.model;

import jakarta.persistence.*;

@Entity
public class EducationsModel extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long education_id;

    private String organization;
    private String degree;
    private String area_study;
    private String start_date;
    private String end_date;
    private String description;



    @ManyToOne
    @JoinColumn(name = "freelancer_id")
    private FreelancersModel freelancerEducations;

    public EducationsModel(Long education_id, String organization, String degree, String area_study, String start_date, String end_date, String description, FreelancersModel freelancerEducations) {
        this.education_id = education_id;
        this.organization = organization;
        this.degree = degree;
        this.area_study = area_study;
        this.start_date = start_date;
        this.end_date = end_date;
        this.description = description;
        this.freelancerEducations = freelancerEducations;
    }

    public Long getEducation_id() {
        return education_id;
    }

    public void setEducation_id(Long education_id) {
        this.education_id = education_id;
    }

    public String getOrganization() {
        return organization;
    }

    public void setOrganization(String organization) {
        this.organization = organization;
    }

    public String getDegree() {
        return degree;
    }

    public void setDegree(String degree) {
        this.degree = degree;
    }

    public String getArea_study() {
        return area_study;
    }

    public void setArea_study(String area_study) {
        this.area_study = area_study;
    }

    public String getStart_date() {
        return start_date;
    }

    public void setStart_date(String start_date) {
        this.start_date = start_date;
    }

    public String getEnd_date() {
        return end_date;
    }

    public void setEnd_date(String end_date) {
        this.end_date = end_date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public FreelancersModel getFreelancerEducations() {
        return freelancerEducations;
    }

    public void setFreelancerEducations(FreelancersModel freelancerEducations) {
        this.freelancerEducations = freelancerEducations;
    }
}
