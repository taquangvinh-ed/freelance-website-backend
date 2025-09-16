package com.freelancemarketplace.backend.repository;

import com.freelancemarketplace.backend.model.AnalyticModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AnalyticsRepository extends JpaRepository<AnalyticModel, Long> {
  }