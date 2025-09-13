package com.freelancemarketplace.backend.model;

import jakarta.persistence.*;

@Entity
public class ReportsModel extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long report_id;

    //The freelancer who is being reported
    @ManyToOne
    @JoinColumn(name = "freelancer_id")
    private FreelancersModel freelancerReports;
}
