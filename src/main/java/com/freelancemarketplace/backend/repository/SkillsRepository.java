package com.freelancemarketplace.backend.repository;

import com.freelancemarketplace.backend.model.SkillModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SkillsRepository extends JpaRepository<SkillModel, Long> {
  }