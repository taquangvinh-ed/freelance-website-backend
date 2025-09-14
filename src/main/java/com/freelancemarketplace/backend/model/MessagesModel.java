package com.freelancemarketplace.backend.model;

import com.freelancemarketplace.backend.enums.MessageStatus;
import jakarta.persistence.*;

import java.sql.Timestamp;

@Entity
public class MessagesModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long message_id;

    private String sender_type;
    private String receiver_type;
    private String content;
    private Timestamp sent_at;

    @Enumerated(EnumType.STRING)
    private MessageStatus status;

    @ManyToOne
    @JoinColumn(name = "freelancer_id")
    private FreelancersModel freelancerMessages;

    @ManyToOne
    @JoinColumn(name = "team_id")
    private TeamsModel teamMessages;

    @ManyToOne
    @JoinColumn(name = "client_id")
    private ClientsModel clientMessages;

    @ManyToOne
    @JoinColumn(name = "company_id")
    private CompaniesModel companyMessages;

    @ManyToOne
    @JoinColumn(name ="proposal_id" )
    private ProposalsModal proposalMessages;

    @ManyToOne
    @JoinColumn(name = "project_id")
    private ProjectsModel projectMessages;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private ProductsModel productMessages;


    public MessagesModel() {
    }

    public Long getMessage_id() {
        return message_id;
    }

    public void setMessage_id(Long message_id) {
        this.message_id = message_id;
    }

    public String getSender_type() {
        return sender_type;
    }

    public void setSender_type(String sender_type) {
        this.sender_type = sender_type;
    }

    public String getReceiver_type() {
        return receiver_type;
    }

    public void setReceiver_type(String receiver_type) {
        this.receiver_type = receiver_type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Timestamp getSent_at() {
        return sent_at;
    }

    public void setSent_at(Timestamp sent_at) {
        this.sent_at = sent_at;
    }

    public MessageStatus getStatus() {
        return status;
    }

    public void setStatus(MessageStatus status) {
        this.status = status;
    }

    public FreelancersModel getFreelancerMessages() {
        return freelancerMessages;
    }

    public void setFreelancerMessages(FreelancersModel freelancerMessages) {
        this.freelancerMessages = freelancerMessages;
    }

    public TeamsModel getTeamMessages() {
        return teamMessages;
    }

    public void setTeamMessages(TeamsModel teamMessages) {
        this.teamMessages = teamMessages;
    }

    public ClientsModel getClientMessages() {
        return clientMessages;
    }

    public void setClientMessages(ClientsModel clientMessages) {
        this.clientMessages = clientMessages;
    }

    public CompaniesModel getCompanyMessages() {
        return companyMessages;
    }

    public void setCompanyMessages(CompaniesModel companyMessages) {
        this.companyMessages = companyMessages;
    }

    public ProposalsModal getProposalMessages() {
        return proposalMessages;
    }

    public void setProposalMessages(ProposalsModal proposalMessages) {
        this.proposalMessages = proposalMessages;
    }

    public ProjectsModel getProjectMessages() {
        return projectMessages;
    }

    public void setProjectMessages(ProjectsModel projectMessages) {
        this.projectMessages = projectMessages;
    }

    public ProductsModel getProductMessages() {
        return productMessages;
    }

    public void setProductMessages(ProductsModel productMessages) {
        this.productMessages = productMessages;
    }
}
