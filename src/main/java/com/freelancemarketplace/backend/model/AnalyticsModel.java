package com.freelancemarketplace.backend.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
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

}
