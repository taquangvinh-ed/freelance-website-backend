package com.freelancemarketplace.backend.repository;

import com.freelancemarketplace.backend.model.ProjectsModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectsRepository extends JpaRepository<ProjectsModel, Long> {
  }