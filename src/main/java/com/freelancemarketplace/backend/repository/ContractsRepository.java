package com.freelancemarketplace.backend.repository;

import com.freelancemarketplace.backend.model.ContractsModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContractsRepository extends JpaRepository<ContractsModel, Long> {
  }