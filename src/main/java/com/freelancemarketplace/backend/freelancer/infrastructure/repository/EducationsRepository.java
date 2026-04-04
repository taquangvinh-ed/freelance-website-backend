package com.freelancemarketplace.backend.freelancer.infrastructure.repository;

import com.freelancemarketplace.backend.freelancer.domain.model.EducationModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EducationsRepository extends JpaRepository<EducationModel, Long> {
  }