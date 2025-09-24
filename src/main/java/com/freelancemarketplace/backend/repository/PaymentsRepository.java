package com.freelancemarketplace.backend.repository;

import com.freelancemarketplace.backend.model.PaymentModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentsRepository extends JpaRepository<PaymentModel, Long> {
  PaymentModel findByTransactionId(String transactionId);
  }