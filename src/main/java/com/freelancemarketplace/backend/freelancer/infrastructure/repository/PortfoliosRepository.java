package com.freelancemarketplace.backend.freelancer.infrastructure.repository;

import com.freelancemarketplace.backend.freelancer.domain.model.PortfolioModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PortfoliosRepository extends JpaRepository<PortfolioModel, Long> {
  }