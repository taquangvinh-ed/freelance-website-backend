package com.freelancemarketplace.backend.model;

import com.freelancemarketplace.backend.enums.MessageStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "Messages")
public class MessageModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long messageId;

    private String senderType;
    private String receiverType;
    private String content;
    private Timestamp sentAt;

    @Enumerated(EnumType.STRING)
    private MessageStatus status;

    @ManyToOne
    @JoinColumn(name = "freelancerId")
    private FreelancerModel freelancer;

    @ManyToOne
    @JoinColumn(name = "teamId")
    private TeamModel team;

    @ManyToOne
    @JoinColumn(name = "clientId")
    private ClientModel client;

    @ManyToOne
    @JoinColumn(name = "companyId")
    private CompanyModel company;

    @ManyToOne
    @JoinColumn(name ="proposalId" )
    private ProposalModal proposal;

    @ManyToOne
    @JoinColumn(name = "projectId")
    private ProjectModel project;

    @ManyToOne
    @JoinColumn(name = "productId")
    private ProductModel product;


}
