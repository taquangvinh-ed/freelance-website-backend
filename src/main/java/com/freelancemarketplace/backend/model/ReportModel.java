package com.freelancemarketplace.backend.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "Reports")
public class ReportModel extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reportId;


    private String reportCategory;

    private String reportReason;

    //The freelancer who is being reported
    @ManyToOne
    @JoinColumn(name = "freelancerId")
    private FreelancerModel freelancer;

    //Company who is being reported
    @ManyToOne
    @JoinColumn(name = "companyId")
    private CompanyModel company;

    @ManyToOne
    @JoinColumn(name = "clientId")
    private ClientModel client;


}
