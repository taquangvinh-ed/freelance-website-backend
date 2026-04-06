package com.freelancemarketplace.backend.contract.domain.model;

import com.freelancemarketplace.backend.contract.domain.enums.ContractStatus;
import com.freelancemarketplace.backend.contract.domain.enums.ContractTypes;
import com.freelancemarketplace.backend.audit.domain.model.BaseEntity;
import com.freelancemarketplace.backend.client.domain.model.ClientModel;
import com.freelancemarketplace.backend.client.domain.model.CompanyModel;
import com.freelancemarketplace.backend.freelancer.domain.model.FreelancerModel;
import com.freelancemarketplace.backend.payment.domain.model.PaymentModel;
import com.freelancemarketplace.backend.project.domain.model.ProjectModel;
import com.freelancemarketplace.backend.proposal.domain.model.ProposalModel;
import com.freelancemarketplace.backend.team.domain.model.TeamModel;
import com.freelancemarketplace.backend.contract.domain.model.MileStoneModel;
import com.freelancemarketplace.backend.contract.domain.model.TimeLog;
import com.freelancemarketplace.backend.contract.domain.model.WeeklyReportModel;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
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

    private Long toggleProjectId;

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

    @OneToMany(mappedBy = "contract")
    private List<TimeLog> timeLog = new ArrayList<>();

    @OneToMany(mappedBy = "contract")
    private List<WeeklyReportModel> weeklyReports;

}
