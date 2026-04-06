package com.freelancemarketplace.backend.report.infrastructure.repository;

import com.freelancemarketplace.backend.report.domain.model.ReportModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReportsRepository extends JpaRepository<ReportModel, Long> {
  }