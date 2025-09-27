package com.freelancemarketplace.backend.dto;

import com.freelancemarketplace.backend.enums.ParticipantType;
import com.freelancemarketplace.backend.model.ConversationModel;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

public class ConversationParticipantDTO {

    private Long conversationParticipantId;

    private String participantType;

    private Long participantId;

}
