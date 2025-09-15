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


}
