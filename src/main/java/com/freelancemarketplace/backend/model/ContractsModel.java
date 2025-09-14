package com.freelancemarketplace.backend.model;

import jakarta.persistence.*;

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
}
