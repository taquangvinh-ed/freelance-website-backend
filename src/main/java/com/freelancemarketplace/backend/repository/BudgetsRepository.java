package com.freelancemarketplace.backend.repository;

import com.freelancemarketplace.backend.project.domain.model.BudgetModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BudgetsRepository extends JpaRepository<BudgetModel, Long> {
  }