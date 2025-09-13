package com.freelancemarketplace.backend.model;

import jakarta.persistence.*;

@Entity
public class ProposalsModal extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long proposal_id;

    @ManyToOne
    @JoinColumn(name = "freelancer_id")
    private FreelancersModel freelancerProposals;
}
