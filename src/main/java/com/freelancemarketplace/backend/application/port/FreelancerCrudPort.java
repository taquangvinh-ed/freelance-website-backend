package com.freelancemarketplace.backend.application.port;

import com.freelancemarketplace.backend.domain.model.FreelancerModel;
import java.util.Optional;

/**
 * Port: Freelancer Repository Interface
 */
public interface FreelancerCrudPort extends BaseCrudPort<FreelancerModel, Long> {
    
    /**
     * Find freelancer by user ID
     * @param userId User ID
     * @return Optional containing freelancer if found
     */
    Optional<FreelancerModel> findByUserId(Long userId);
}

