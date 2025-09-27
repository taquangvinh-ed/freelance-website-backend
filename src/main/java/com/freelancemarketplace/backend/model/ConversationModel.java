package com.freelancemarketplace.backend.model;

import com.freelancemarketplace.backend.enums.ConversationType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "conversation")
public class ConversationModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long conversationId;

    @Enumerated(EnumType.STRING)
    private ConversationType type; //ONE_TO_ONE, GROUP

    @OneToMany(mappedBy = "conversation")
    private List<ConversationParticipantModel> participant = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "teamId")
    private TeamModel team; // For team-based conversations
}
