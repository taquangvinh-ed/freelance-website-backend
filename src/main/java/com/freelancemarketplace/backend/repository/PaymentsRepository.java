package com.freelancemarketplace.backend.repository;

import com.freelancemarketplace.backend.model.PaymentModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PaymentsRepository extends JpaRepository<PaymentModel, Long> {
  PaymentModel findByTransactionId(String transactionId);
  List<PaymentModel> findTop5ByContractClientClientIdOrderByPaidAtDesc(Long clientId);
  }