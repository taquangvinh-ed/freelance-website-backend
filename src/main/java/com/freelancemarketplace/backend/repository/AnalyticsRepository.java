package com.freelancemarketplace.backend.repository;

import com.freelancemarketplace.backend.model.AnalyticsModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AnalyticsRepository extends JpaRepository<AnalyticsModel, Long> {
  }