package com.freelancemarketplace.backend.model;

import jakarta.persistence.*;

import java.util.Set;

@Entity
public class ContractsModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long contract_id;

    @ManyToOne
    @JoinColumn(name = "freelancer_id")
    private FreelancersModel contractFreelancer;

    @OneToOne(mappedBy = "contractProposal")
    private ProposalsModal proposalContract;

    //The payments that belong to a contract
    @OneToMany(mappedBy = "contractPayments")
    private Set<PaymentsModel> paymentsContract;

}
