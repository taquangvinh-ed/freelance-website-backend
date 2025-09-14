package com.freelancemarketplace.backend.repository;

import com.freelancemarketplace.backend.model.PaymentsModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentsRepository extends JpaRepository<PaymentsModel, Long> {
  }