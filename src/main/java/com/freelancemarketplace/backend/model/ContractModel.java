package com.freelancemarketplace.backend.model;

import com.freelancemarketplace.backend.enums.ContractStatus;
import com.freelancemarketplace.backend.enums.ContractTypes;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "Contracts")
public class ContractModel extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long contractId;

    @Enumerated(EnumType.STRING)
    private ContractTypes types;

    private double amount;

    private Timestamp startDate;

    private Timestamp endDate;

    @Enumerated(EnumType.STRING)
    private ContractStatus status;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "proposalId")
    private ProposalModel proposal;

    //The payments that belong to a contract
    @OneToMany(mappedBy = "contract", orphanRemoval = true)
    private Set<PaymentModel> payments = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "freelancerId")
    private FreelancerModel freelancer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "teamId")
    private TeamModel team;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "companyId")
    private CompanyModel company;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "clientId")
    private ClientModel client;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "projectId")
    private ProjectModel contractProject;

    @OneToMany(mappedBy = "contract", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<MileStoneModel> mileStones = new HashSet<>();

}
