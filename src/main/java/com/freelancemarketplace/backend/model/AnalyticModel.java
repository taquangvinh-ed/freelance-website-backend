package com.freelancemarketplace.backend.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name="Analytics")
public class AnalyticModel extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long analyticId;

    private Long earnings;

    private Long spent;

    private Integer jobCount;

    private Integer hourCount;

    private Long views;

    @OneToOne(mappedBy = "analytic")
    private ClientModel client;

    @OneToOne(mappedBy = "analytic")
    private FreelancerModel freelancer;



}
