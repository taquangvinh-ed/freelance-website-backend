package com.freelancemarketplace.backend.repository;

import com.freelancemarketplace.backend.model.EducationsModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EducationsRepository extends JpaRepository<EducationsModel, Long> {
  }