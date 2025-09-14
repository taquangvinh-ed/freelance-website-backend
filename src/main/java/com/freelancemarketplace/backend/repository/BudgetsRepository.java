package com.freelancemarketplace.backend.repository;

import com.freelancemarketplace.backend.model.BudgetsModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BudgetsRepository extends JpaRepository<BudgetsModel, Long> {
  }