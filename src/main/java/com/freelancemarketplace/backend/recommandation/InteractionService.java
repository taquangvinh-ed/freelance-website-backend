package com.freelancemarketplace.backend.recommandation;

import com.freelancemarketplace.backend.enums.InteractionType;

public interface InteractionService {

    public void logInteraction(Long freelancerId, Long projectId, InteractionType interactionType);
}
