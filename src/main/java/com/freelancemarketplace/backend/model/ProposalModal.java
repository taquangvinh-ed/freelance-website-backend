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
@Table(name = "Proposals")
public class ProposalModal extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long proposalId;

    // The name of the proposal
    private String name;

    private String description;

    private String[] files;

    private String status;

    private Double price;

    @ManyToOne
    @JoinColumn(name = "freelancerId")
    private FreelancerModel freelancer;

    @ManyToOne
    @JoinColumn(name = "projectId")
    private ProjectModel project;

    @OneToMany(mappedBy = "proposal")
    private Set<MessageModel> messagesList;

    @OneToOne
    @JoinColumn(name = "contractId")
    private ContractModel contract;

    @ManyToOne
    @JoinColumn(name = "teamId")
    private TeamModel team;

}
