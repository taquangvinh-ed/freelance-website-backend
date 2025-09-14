package com.freelancemarketplace.backend.model;

import jakarta.persistence.*;

import java.util.Set;

@Entity
public class LocationsModel extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long location_id;

    private String postcode;
    private String country;
    private String state;
    private String city;
    private String details;


    @OneToMany(mappedBy = "admin_location")
    private Set<AdminsModel> adminList;

    @OneToMany(mappedBy = "company_location")
    private Set<CompaniesModel> companyList;

    @OneToMany(mappedBy = "freelancer_location")
    private Set<FreelancersModel> freelancerList;

    @OneToMany(mappedBy = "client_location")
    private Set<ClientsModel> clientList;

    @OneToMany(mappedBy = "experience_location")
    private Set<ExperiencesModel> experienceList;

    public LocationsModel(Long location_id, String postcode, String country, String state, String city, String details, Set<AdminsModel> adminList, Set<CompaniesModel> companyList, Set<FreelancersModel> freelancerList, Set<ClientsModel> clientList, Set<ExperiencesModel> experienceList) {
        this.location_id = location_id;
        this.postcode = postcode;
        this.country = country;
        this.state = state;
        this.city = city;
        this.details = details;
        this.adminList = adminList;
        this.companyList = companyList;
        this.freelancerList = freelancerList;
        this.clientList = clientList;
        this.experienceList = experienceList;
    }

    public Long getLocation_id() {
        return location_id;
    }

    public void setLocation_id(Long location_id) {
        this.location_id = location_id;
    }

    public String getPostcode() {
        return postcode;
    }

    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public Set<AdminsModel> getAdminList() {
        return adminList;
    }

    public void setAdminList(Set<AdminsModel> adminList) {
        this.adminList = adminList;
    }

    public Set<CompaniesModel> getCompanyList() {
        return companyList;
    }

    public void setCompanyList(Set<CompaniesModel> companyList) {
        this.companyList = companyList;
    }

    public Set<FreelancersModel> getFreelancerList() {
        return freelancerList;
    }

    public void setFreelancerList(Set<FreelancersModel> freelancerList) {
        this.freelancerList = freelancerList;
    }

    public Set<ClientsModel> getClientList() {
        return clientList;
    }

    public void setClientList(Set<ClientsModel> clientList) {
        this.clientList = clientList;
    }

    public Set<ExperiencesModel> getExperienceList() {
        return experienceList;
    }

    public void setExperienceList(Set<ExperiencesModel> experienceList) {
        this.experienceList = experienceList;
    }
}
