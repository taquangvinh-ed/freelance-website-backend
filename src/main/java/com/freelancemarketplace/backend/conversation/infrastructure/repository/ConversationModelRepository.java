package com.freelancemarketplace.backend.conversation.infrastructure.repository;

import com.freelancemarketplace.backend.conversation.domain.model.ConversationModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConversationModelRepository extends JpaRepository<ConversationModel, Long> {
}