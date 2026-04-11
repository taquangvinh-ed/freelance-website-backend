package com.freelancemarketplace.backend.client.application.port;

import com.freelancemarketplace.backend.client.domain.model.ClientModel;
import com.freelancemarketplace.backend.project.application.port.BaseCrudPort;
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
