package com.freelancemarketplace.backend.repository;

import com.freelancemarketplace.backend.domain.model.EducationModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EducationsRepository extends JpaRepository<EducationModel, Long> {
  }