package com.freelancemarketplace.backend.repository;

import com.freelancemarketplace.backend.model.ConversationModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConversationModelRepository extends JpaRepository<ConversationModel, Long> {
}