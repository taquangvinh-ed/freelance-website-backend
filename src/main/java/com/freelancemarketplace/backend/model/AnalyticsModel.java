package com.freelancemarketplace.backend.model;

import jakarta.persistence.*;

@Entity
public class AnalyticsModel extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long analytics_id;

    private Long earnings;

    private Long spent;

    private Integer job_count;

    private Integer hour_count;

    private Long views;

    @OneToOne(mappedBy = "client_analytics")
    private ClientsModel client;

    @OneToOne(mappedBy = "freelancer_analytics")
    private FreelancersModel freelancer;

    @OneToOne(mappedBy = "company_analytics")
    private CompaniesModel company;

    public AnalyticsModel() {
    }

    public Long getAnalytics_id() {
        return analytics_id;
    }

    public void setAnalytics_id(Long analytics_id) {
        this.analytics_id = analytics_id;
    }

    public Long getEarnings() {
        return earnings;
    }

    public void setEarnings(Long earnings) {
        this.earnings = earnings;
    }

    public Long getSpent() {
        return spent;
    }

    public void setSpent(Long spent) {
        this.spent = spent;
    }

    public Integer getJob_count() {
        return job_count;
    }

    public void setJob_count(Integer job_count) {
        this.job_count = job_count;
    }

    public Integer getHour_count() {
        return hour_count;
    }

    public void setHour_count(Integer hour_count) {
        this.hour_count = hour_count;
    }

    public Long getViews() {
        return views;
    }

    public void setViews(Long views) {
        this.views = views;
    }

    public ClientsModel getClient() {
        return client;
    }

    public void setClient(ClientsModel client) {
        this.client = client;
    }

    public FreelancersModel getFreelancer() {
        return freelancer;
    }

    public void setFreelancer(FreelancersModel freelancer) {
        this.freelancer = freelancer;
    }

    public CompaniesModel getCompany() {
        return company;
    }

    public void setCompany(CompaniesModel company) {
        this.company = company;
    }

    public AnalyticsModel(Long analytics_id, Long earnings, Long spent, Integer job_count, Integer hour_count, Long views, ClientsModel client, FreelancersModel freelancer, CompaniesModel company) {
        this.analytics_id = analytics_id;
        this.earnings = earnings;
        this.spent = spent;
        this.job_count = job_count;
        this.hour_count = hour_count;
        this.views = views;
        this.client = client;
        this.freelancer = freelancer;
        this.company = company;


    }
}
