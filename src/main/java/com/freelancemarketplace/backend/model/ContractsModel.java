package com.freelancemarketplace.backend.model;

import com.freelancemarketplace.backend.enums.ContractStatus;
import com.freelancemarketplace.backend.enums.ContractTypes;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class ContractsModel extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long contract_id;

    @Enumerated(EnumType.STRING)
    private ContractTypes types;

    private double amount;

    private Timestamp start_date;

    private Timestamp end_date;

    @Enumerated(EnumType.STRING)
    private ContractStatus status;

    @ManyToOne
    @JoinColumn(name = "freelancer_id")
    private FreelancersModel contractFreelancer;

    @OneToOne(mappedBy = "contractProposal")
    private ProposalsModal proposalContract;

    //The payments that belong to a contract
    @OneToMany(mappedBy = "contractPayments")
    private Set<PaymentsModel> paymentsContract;

    @ManyToOne
    @JoinColumn(name = "team_id")
    private TeamsModel contractTeam;

    @ManyToOne
    @JoinColumn(name = "company_id")
    private CompaniesModel contractCompany;

    @ManyToOne
    @JoinColumn(name = "client_id")
    private ClientsModel contractClient;

    @OneToOne
    @JoinColumn(name = "project_id")
    private ProjectsModel contractProject;

}
