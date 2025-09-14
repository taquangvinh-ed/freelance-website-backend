package com.freelancemarketplace.backend.model;

import jakarta.persistence.*;

@Entity
public class ReportsModel extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long report_id;


    private String report_category;
    private String report_reason;

    //The freelancer who is being reported
    @ManyToOne
    @JoinColumn(name = "freelancer_id")
    private FreelancersModel freelancerReports;

    //Company who is being reported
    @ManyToOne
    @JoinColumn(name = "company_id")
    private CompaniesModel companyReports;

    @ManyToOne
    @JoinColumn(name = "client_id")
    private ClientsModel client_reports;

    public ReportsModel() {
    }

    public Long getReport_id() {
        return report_id;
    }

    public void setReport_id(Long report_id) {
        this.report_id = report_id;
    }

    public String getReport_category() {
        return report_category;
    }

    public void setReport_category(String report_category) {
        this.report_category = report_category;
    }

    public String getReport_reason() {
        return report_reason;
    }

    public void setReport_reason(String report_reason) {
        this.report_reason = report_reason;
    }

    public FreelancersModel getFreelancerReports() {
        return freelancerReports;
    }

    public void setFreelancerReports(FreelancersModel freelancerReports) {
        this.freelancerReports = freelancerReports;
    }

    public CompaniesModel getCompanyReports() {
        return companyReports;
    }

    public void setCompanyReports(CompaniesModel companyReports) {
        this.companyReports = companyReports;
    }

    public ClientsModel getClient_reports() {
        return client_reports;
    }

    public void setClient_reports(ClientsModel client_reports) {
        this.client_reports = client_reports;
    }
}
