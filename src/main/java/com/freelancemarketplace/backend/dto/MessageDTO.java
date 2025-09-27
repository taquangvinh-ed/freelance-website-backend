package com.freelancemarketplace.backend.dto;

public class MessageDTO {

    private Long messageId;
    private Long conversationId; // Links to ConversationModel
    private String senderType; // "FREELANCER", "CLIENT", "COMPANY"
    private Long senderId; // ID of sender entity
    private String content;
    private String status; // "SENT", "DELIVERED", "READ"
    private Long proposalId; // Optional, links to ProposalModel
    private Long projectId; // Optional
    private Long productId;
}
