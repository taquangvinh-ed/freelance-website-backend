package com.freelancemarketplace.backend.project.infrastructure.repository;

import com.freelancemarketplace.backend.project.domain.enums.ProjectStatus;
import com.freelancemarketplace.backend.client.domain.model.ClientModel;
import com.freelancemarketplace.backend.project.domain.model.ProjectModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ProjectsRepository extends JpaRepository<ProjectModel, Long>, JpaSpecificationExecutor<ProjectModel> {
    List<ProjectModel> findAllByClient(ClientModel clientModel);
    long countByCreatedAtAfter(LocalDateTime afterDate);
    long countByStatus(ProjectStatus status);
    List<ProjectModel>findByStatus(ProjectStatus status);
    List<ProjectModel> findAllByClientAndStatus(ClientModel clientModel, ProjectStatus status);
}