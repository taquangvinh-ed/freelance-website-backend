package com.freelancemarketplace.backend.conversation.domain.model;

import com.freelancemarketplace.backend.conversation.domain.enums.ConversationType;
import com.freelancemarketplace.backend.conversation.domain.enums.ParticipantType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "conversationParticipant")
public class ConversationParticipantModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long conversationParticipantId;

    @ManyToOne
    @JoinColumn(name = "conversationId")
    private ConversationModel conversation;

    private ParticipantType participantType;

    private Long participantId;


}
