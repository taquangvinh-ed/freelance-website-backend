package com.freelancemarketplace.backend.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
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


}
