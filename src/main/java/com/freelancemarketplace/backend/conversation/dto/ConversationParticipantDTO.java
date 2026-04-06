package com.freelancemarketplace.backend.conversation.dto;

import com.freelancemarketplace.backend.conversation.domain.enums.ParticipantType;
import com.freelancemarketplace.backend.conversation.domain.model.ConversationModel;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

public class ConversationParticipantDTO {

    private Long conversationParticipantId;

    private String participantType;

    private Long participantId;

}
