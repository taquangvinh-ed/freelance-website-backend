package com.freelancemarketplace.backend.contract.application.service.imp;

import com.freelancemarketplace.backend.contract.dto.HourlyPaymentDetailDTO;
import com.freelancemarketplace.backend.contract.dto.HourlyPaymentSummaryDTO;
import com.freelancemarketplace.backend.payment.domain.enums.PaymentStatus;
import com.freelancemarketplace.backend.exceptionHandling.ApiException;
import com.freelancemarketplace.backend.exceptionHandling.ErrorCode;
import com.freelancemarketplace.backend.exceptionHandling.ResourceNotFoundException;
import com.freelancemarketplace.backend.contract.domain.model.ContractModel;
import com.freelancemarketplace.backend.payment.domain.model.PaymentModel;
import com.freelancemarketplace.backend.contract.infrastructure.repository.ContractsRepository;
import com.freelancemarketplace.backend.payment.infrastructure.repository.PaymentsRepository;
import com.freelancemarketplace.backend.contract.application.service.HourlyPaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
public class HourlyPaymentServiceImp implements HourlyPaymentService {

    private static final String HOURLY_TX_PREFIX = "HOURLY-";

    private final PaymentsRepository paymentsRepository;
    private final ContractsRepository contractsRepository;

    @Override
    public List<HourlyPaymentSummaryDTO> getHourlyPaymentsByContract(Long contractId, Long currentUserId) {
        ContractModel contract = getContractOrThrow(contractId);
        ensureParticipant(contract, currentUserId);

        return paymentsRepository.findByContractContractIdOrderByPaidAtDesc(contractId).stream()
                .filter(this::isHourlyPayment)
                .map(this::toSummaryDto)
                .toList();
    }

    @Override
    public HourlyPaymentDetailDTO getHourlyPaymentById(Long paymentId, Long currentUserId) {
        PaymentModel payment = getHourlyPaymentOrThrow(paymentId);
        ensureParticipant(payment.getContract(), currentUserId);
        return toDetailDto(payment);
    }

    @Override
    public HourlyPaymentDetailDTO approveHourlyPayment(Long paymentId, Long currentUserId, String note) {
        PaymentModel payment = getHourlyPaymentOrThrow(paymentId);
        ensureClientOwner(payment.getContract(), currentUserId);

        if (payment.getStatus() != PaymentStatus.PENDING) {
            throw new ApiException(HttpStatus.BAD_REQUEST, ErrorCode.INVALID_OPERATION,
                    "Only PENDING hourly payment can be approved");
        }

        payment.setStatus(PaymentStatus.COMPLETED);
        payment.setPaidAt(Timestamp.from(Instant.now()));
        PaymentModel saved = paymentsRepository.save(payment);

        HourlyPaymentDetailDTO dto = toDetailDto(saved);
        dto.setNote(note);
        return dto;
    }

    @Override
    public HourlyPaymentDetailDTO disputeHourlyPayment(Long paymentId, Long currentUserId, String note) {
        PaymentModel payment = getHourlyPaymentOrThrow(paymentId);
        ensureClientOwner(payment.getContract(), currentUserId);

        if (payment.getStatus() != PaymentStatus.PENDING) {
            throw new ApiException(HttpStatus.BAD_REQUEST, ErrorCode.INVALID_OPERATION,
                    "Only PENDING hourly payment can be disputed");
        }

        payment.setStatus(PaymentStatus.FAILED);
        PaymentModel saved = paymentsRepository.save(payment);

        HourlyPaymentDetailDTO dto = toDetailDto(saved);
        dto.setNote(note);
        return dto;
    }

    private ContractModel getContractOrThrow(Long contractId) {
        return contractsRepository.findById(contractId)
                .orElseThrow(() -> new ResourceNotFoundException("Contract with id: " + contractId + " not found"));
    }

    private PaymentModel getHourlyPaymentOrThrow(Long paymentId) {
        PaymentModel payment = paymentsRepository.findById(paymentId)
                .orElseThrow(() -> new ResourceNotFoundException("Payment with id: " + paymentId + " not found"));

        if (!isHourlyPayment(payment)) {
            throw new ResourceNotFoundException("Hourly payment with id: " + paymentId + " not found");
        }

        return payment;
    }

    private boolean isHourlyPayment(PaymentModel payment) {
        return payment.getTransactionId() != null && payment.getTransactionId().startsWith(HOURLY_TX_PREFIX);
    }

    private void ensureParticipant(ContractModel contract, Long currentUserId) {
        Long clientId = contract.getClient() != null ? contract.getClient().getClientId() : null;
        Long freelancerId = contract.getFreelancer() != null ? contract.getFreelancer().getFreelancerId() : null;

        if (!currentUserId.equals(clientId) && !currentUserId.equals(freelancerId)) {
            throw new ApiException(HttpStatus.FORBIDDEN, ErrorCode.FORBIDDEN,
                    "You are not allowed to access this contract's hourly payments");
        }
    }

    private void ensureClientOwner(ContractModel contract, Long currentUserId) {
        Long clientId = contract.getClient() != null ? contract.getClient().getClientId() : null;
        if (!currentUserId.equals(clientId)) {
            throw new ApiException(HttpStatus.FORBIDDEN, ErrorCode.FORBIDDEN,
                    "Only contract owner can perform this action");
        }
    }

    private HourlyPaymentSummaryDTO toSummaryDto(PaymentModel payment) {
        HourlyPaymentSummaryDTO dto = new HourlyPaymentSummaryDTO();
        dto.setPaymentId(payment.getPaymentId());
        dto.setContractId(payment.getContract() != null ? payment.getContract().getContractId() : null);
        dto.setAmount(payment.getAmount());
        dto.setStatus(payment.getStatus() != null ? payment.getStatus().name() : null);
        dto.setTransactionId(payment.getTransactionId());
        dto.setPaidAt(payment.getPaidAt());
        return dto;
    }

    private HourlyPaymentDetailDTO toDetailDto(PaymentModel payment) {
        HourlyPaymentDetailDTO dto = new HourlyPaymentDetailDTO();
        dto.setPaymentId(payment.getPaymentId());
        dto.setContractId(payment.getContract() != null ? payment.getContract().getContractId() : null);
        dto.setAmount(payment.getAmount());
        dto.setStatus(payment.getStatus() != null ? payment.getStatus().name() : null);
        dto.setTransactionId(payment.getTransactionId());
        dto.setPaidAt(payment.getPaidAt());
        dto.setClientId(payment.getClient() != null ? payment.getClient().getClientId() : null);
        dto.setFreelancerId(payment.getFreelancer() != null ? payment.getFreelancer().getFreelancerId() : null);
        return dto;
    }
}

