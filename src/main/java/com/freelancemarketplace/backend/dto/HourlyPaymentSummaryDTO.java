package com.freelancemarketplace.backend.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.sql.Timestamp;

@Getter
@Setter
@NoArgsConstructor
public class HourlyPaymentSummaryDTO {
    private Long paymentId;
    private Long contractId;
    private BigDecimal amount;
    private String status;
    private String transactionId;
    private Timestamp paidAt;
}

