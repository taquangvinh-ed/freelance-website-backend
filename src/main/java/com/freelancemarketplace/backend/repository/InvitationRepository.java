package com.freelancemarketplace.backend.repository;

import com.freelancemarketplace.backend.model.FreelancerModel;
import com.freelancemarketplace.backend.model.InvitationModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InvitationRepository extends JpaRepository<InvitationModel, Long> {
    Boolean existsByFreelancer(FreelancerModel freelancerModel);
}