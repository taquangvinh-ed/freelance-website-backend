package com.freelancemarketplace.backend.recommendation.infrastructure.repository;

import com.freelancemarketplace.backend.project.domain.model.ProjectInteractionModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectInteractionModelRepository extends JpaRepository<ProjectInteractionModel, Long> {
}