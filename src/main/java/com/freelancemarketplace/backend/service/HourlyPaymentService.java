package com.freelancemarketplace.backend.service;

import com.freelancemarketplace.backend.dto.HourlyPaymentDetailDTO;
import com.freelancemarketplace.backend.dto.HourlyPaymentSummaryDTO;

import java.util.List;

public interface HourlyPaymentService {
    List<HourlyPaymentSummaryDTO> getHourlyPaymentsByContract(Long contractId, Long currentUserId);

    HourlyPaymentDetailDTO getHourlyPaymentById(Long paymentId, Long currentUserId);

    HourlyPaymentDetailDTO approveHourlyPayment(Long paymentId, Long currentUserId, String note);

    HourlyPaymentDetailDTO disputeHourlyPayment(Long paymentId, Long currentUserId, String note);
}

