package com.freelancemarketplace.backend.repository;

import com.freelancemarketplace.backend.model.ExperienceModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExperiencesRepository extends JpaRepository<ExperienceModel, Long> {
  }