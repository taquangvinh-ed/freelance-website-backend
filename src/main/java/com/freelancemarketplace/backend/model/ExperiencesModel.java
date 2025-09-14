package com.freelancemarketplace.backend.model;

import jakarta.persistence.*;

@Entity
public class ExperiencesModel extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long experience_id;

    private String title;
    private String organization;
    private String start_date;
    private String end_date;
    private String description;


    @ManyToOne
    @JoinColumn(name = "location_id")
    private LocationsModel experience_location;

    //The experiences that belong to a freelancer
    @ManyToOne
    @JoinColumn(name = "freelancer_id")
    private FreelancersModel freelancerExperiences;

    public ExperiencesModel() {
    }

    public Long getExperience_id() {
        return experience_id;
    }

    public void setExperience_id(Long experience_id) {
        this.experience_id = experience_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getOrganization() {
        return organization;
    }

    public void setOrganization(String organization) {
        this.organization = organization;
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

    public LocationsModel getExperience_location() {
        return experience_location;
    }

    public void setExperience_location(LocationsModel experience_location) {
        this.experience_location = experience_location;
    }

    public FreelancersModel getFreelancerExperiences() {
        return freelancerExperiences;
    }

    public void setFreelancerExperiences(FreelancersModel freelancerExperiences) {
        this.freelancerExperiences = freelancerExperiences;
    }
}
