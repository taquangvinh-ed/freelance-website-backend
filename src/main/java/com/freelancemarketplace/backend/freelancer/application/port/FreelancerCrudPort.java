package com.freelancemarketplace.backend.freelancer.application.port;

import com.freelancemarketplace.backend.freelancer.domain.model.FreelancerModel;
import com.freelancemarketplace.backend.project.application.port.BaseCrudPort;
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
