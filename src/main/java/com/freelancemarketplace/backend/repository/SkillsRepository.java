package com.freelancemarketplace.backend.repository;

import com.freelancemarketplace.backend.model.SkillsModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SkillsRepository extends JpaRepository<SkillsModel, Long> {
  }