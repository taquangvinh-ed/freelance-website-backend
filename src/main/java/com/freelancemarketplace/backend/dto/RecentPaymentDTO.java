package com.freelancemarketplace.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.sql.Timestamp;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RecentPaymentDTO {
    private Long id;
    private BigDecimal amount;
    private String projectTitle; // Tên dự án liên quan đến thanh toán
    private Timestamp paymentDate; // Sử dụng String để định dạng ngày ở Backend (hoặc Frontend)
    private String status;
}
