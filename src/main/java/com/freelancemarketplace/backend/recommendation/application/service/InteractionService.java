package com.freelancemarketplace.backend.recommendation.application.service;

import com.freelancemarketplace.backend.recommendation.domain.enums.InteractionType;

public interface InteractionService {

    void logInteraction(Long freelancerId, Long projectId, InteractionType interactionType);
}
