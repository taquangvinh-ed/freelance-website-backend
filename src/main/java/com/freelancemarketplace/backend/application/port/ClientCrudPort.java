package com.freelancemarketplace.backend.application.port;

import com.freelancemarketplace.backend.client.domain.model.ClientModel;
import java.util.Optional;

/**
 * Port: Client Repository Interface
 */
public interface ClientCrudPort extends BaseCrudPort<ClientModel, Long> {
    
    /**
     * Find client by user ID
     * @param userId User ID
     * @return Optional containing client if found
     */
    Optional<ClientModel> findByUserId(Long userId);
}

