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

    @OneToOne(mappedBy = "contract")
    private ProposalModal proposal;

    //The payments that belong to a contract
    @OneToMany(mappedBy = "contract")
    private Set<PaymentModel> payments;

    @ManyToOne
    @JoinColumn(name = "freelancerId")
    private FreelancerModel freelancer;

    @ManyToOne
    @JoinColumn(name = "teamId")
    private TeamModel team;

    @ManyToOne
    @JoinColumn(name = "companyId")
    private CompanyModel company;

    @ManyToOne
    @JoinColumn(name = "clientId")
    private ClientModel client;

    @OneToOne
    @JoinColumn(name = "projectId")
    private ProjectModel contractProject;

}
