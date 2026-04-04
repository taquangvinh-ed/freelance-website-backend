package com.freelancemarketplace.backend.conversation.infrastructure.repository;

import com.freelancemarketplace.backend.conversation.domain.model.ConversationParticipantModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConversationParticipantModelRepository extends JpaRepository<ConversationParticipantModel, Long> {
}