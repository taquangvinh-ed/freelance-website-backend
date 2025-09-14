package com.freelancemarketplace.backend.repository;

import com.freelancemarketplace.backend.model.CompaniesModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CompaniesRepository extends JpaRepository<CompaniesModel, Long> {
  }