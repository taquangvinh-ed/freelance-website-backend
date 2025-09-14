package com.freelancemarketplace.backend.model;

import jakarta.persistence.*;

import java.util.Set;

@Entity
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

    public ProposalsModal(Long proposal_id, String name, String description, String[] files, String status, Double price, FreelancersModel freelancerProposals, ProjectsModel projectProposals, Set<MessagesModel> messagesList, ContractsModel contractProposal, TeamsModel teamProposals) {
        this.proposal_id = proposal_id;
        this.name = name;
        this.description = description;
        this.files = files;
        this.status = status;
        this.price = price;
        this.freelancerProposals = freelancerProposals;
        this.projectProposals = projectProposals;
        this.messagesList = messagesList;
        this.contractProposal = contractProposal;
        this.teamProposals = teamProposals;
    }

    public Long getProposal_id() {
        return proposal_id;
    }

    public void setProposal_id(Long proposal_id) {
        this.proposal_id = proposal_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String[] getFiles() {
        return files;
    }

    public void setFiles(String[] files) {
        this.files = files;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public FreelancersModel getFreelancerProposals() {
        return freelancerProposals;
    }

    public void setFreelancerProposals(FreelancersModel freelancerProposals) {
        this.freelancerProposals = freelancerProposals;
    }

    public ProjectsModel getProjectProposals() {
        return projectProposals;
    }

    public void setProjectProposals(ProjectsModel projectProposals) {
        this.projectProposals = projectProposals;
    }

    public Set<MessagesModel> getMessagesList() {
        return messagesList;
    }

    public void setMessagesList(Set<MessagesModel> messagesList) {
        this.messagesList = messagesList;
    }

    public ContractsModel getContractProposal() {
        return contractProposal;
    }

    public void setContractProposal(ContractsModel contractProposal) {
        this.contractProposal = contractProposal;
    }

    public TeamsModel getTeamProposals() {
        return teamProposals;
    }

    public void setTeamProposals(TeamsModel teamProposals) {
        this.teamProposals = teamProposals;
    }
}
