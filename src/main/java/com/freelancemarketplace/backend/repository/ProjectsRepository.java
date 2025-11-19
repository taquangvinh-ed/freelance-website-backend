package com.freelancemarketplace.backend.repository;

import com.freelancemarketplace.backend.enums.ProjectStatus;
import com.freelancemarketplace.backend.model.ClientModel;
import com.freelancemarketplace.backend.model.ProjectModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
}