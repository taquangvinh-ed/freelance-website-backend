package com.freelancemarketplace.backend.repository;

import com.freelancemarketplace.backend.model.ReportsModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReportsRepository extends JpaRepository<ReportsModel, Long> {
  }