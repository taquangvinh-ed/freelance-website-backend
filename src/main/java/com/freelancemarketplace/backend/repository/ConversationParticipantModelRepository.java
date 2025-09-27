package com.freelancemarketplace.backend.repository;

import com.freelancemarketplace.backend.model.ConversationParticipantModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConversationParticipantModelRepository extends JpaRepository<ConversationParticipantModel, Long> {
}