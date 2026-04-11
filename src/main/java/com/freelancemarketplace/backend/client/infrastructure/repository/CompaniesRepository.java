package com.freelancemarketplace.backend.client.infrastructure.repository;

import com.freelancemarketplace.backend.client.domain.model.CompanyModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CompaniesRepository extends JpaRepository<CompanyModel, Long> {
  }