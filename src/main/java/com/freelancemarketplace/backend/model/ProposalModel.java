package com.freelancemarketplace.backend.model;

import com.freelancemarketplace.backend.enums.ProposalStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "Proposals")
public class ProposalModel extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long proposalId;

    // The name of the proposal
    private String name;

    private String description;

    private String[] files;

    @Enumerated(EnumType.STRING)
    private ProposalStatus status;

    private BigDecimal amount;

    private Integer deliveryDays;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "freelancerId")
    private FreelancerModel freelancer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "projectId", nullable = false)
    private ProjectModel project;

    @OneToMany(mappedBy = "proposal")
    private Set<MessageModel> messages;

    @OneToOne(mappedBy = "proposal")
    private ContractModel contract;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "teamId")
    private TeamModel team;

}
