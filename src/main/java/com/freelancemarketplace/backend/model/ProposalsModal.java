package com.freelancemarketplace.backend.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class ProposalsModal extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long proposal_id;

    // The name of the proposal
    private String name;

    private String description;

    private String[] files;

    private String status;

    private Double price;

    @ManyToOne
    @JoinColumn(name = "freelancer_id")
    private FreelancersModel freelancerProposals;

    @ManyToOne
    @JoinColumn(name = "project_id")
    private ProjectsModel projectProposals;

    @OneToMany(mappedBy = "proposalMessages" )
    private Set<MessagesModel> messagesList;

    @OneToOne
    @JoinColumn(name = "contract_id")
    private ContractsModel contractProposal;

    @ManyToOne
    @JoinColumn(name = "team_id")
    private TeamsModel teamProposals;

}
