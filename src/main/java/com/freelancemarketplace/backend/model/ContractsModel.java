package com.freelancemarketplace.backend.model;

import com.freelancemarketplace.backend.enums.ContractStatus;
import com.freelancemarketplace.backend.enums.ContractTypes;
import jakarta.persistence.*;

import java.sql.Timestamp;
import java.util.Set;

@Entity
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

    public ContractsModel() {
    }

    public Long getContract_id() {
        return contract_id;
    }

    public void setContract_id(Long contract_id) {
        this.contract_id = contract_id;
    }

    public ContractTypes getTypes() {
        return types;
    }

    public void setTypes(ContractTypes types) {
        this.types = types;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public Timestamp getStart_date() {
        return start_date;
    }

    public void setStart_date(Timestamp start_date) {
        this.start_date = start_date;
    }

    public Timestamp getEnd_date() {
        return end_date;
    }

    public void setEnd_date(Timestamp end_date) {
        this.end_date = end_date;
    }

    public ContractStatus getStatus() {
        return status;
    }

    public void setStatus(ContractStatus status) {
        this.status = status;
    }

    public FreelancersModel getContractFreelancer() {
        return contractFreelancer;
    }

    public void setContractFreelancer(FreelancersModel contractFreelancer) {
        this.contractFreelancer = contractFreelancer;
    }

    public ProposalsModal getProposalContract() {
        return proposalContract;
    }

    public void setProposalContract(ProposalsModal proposalContract) {
        this.proposalContract = proposalContract;
    }

    public Set<PaymentsModel> getPaymentsContract() {
        return paymentsContract;
    }

    public void setPaymentsContract(Set<PaymentsModel> paymentsContract) {
        this.paymentsContract = paymentsContract;
    }

    public TeamsModel getContractTeam() {
        return contractTeam;
    }

    public void setContractTeam(TeamsModel contractTeam) {
        this.contractTeam = contractTeam;
    }

    public CompaniesModel getContractCompany() {
        return contractCompany;
    }

    public void setContractCompany(CompaniesModel contractCompany) {
        this.contractCompany = contractCompany;
    }

    public ClientsModel getContractClient() {
        return contractClient;
    }

    public void setContractClient(ClientsModel contractClient) {
        this.contractClient = contractClient;
    }

    public ProjectsModel getContractProject() {
        return contractProject;
    }

    public void setContractProject(ProjectsModel contractProject) {
        this.contractProject = contractProject;
    }
}
