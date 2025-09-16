package com.freelancemarketplace.backend.repository;

import com.freelancemarketplace.backend.model.PortfolioModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PortfoliosRepository extends JpaRepository<PortfolioModel, Long> {
  }