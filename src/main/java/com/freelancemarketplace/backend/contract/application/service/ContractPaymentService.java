package com.freelancemarketplace.backend.contract.application.service;

import com.freelancemarketplace.backend.contract.dto.MileStoneDTO;
import jakarta.transaction.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.sql.Timestamp;

public interface ContractPaymentService {

    @Transactional
    MileStoneDTO processMilestonePayment(Long contractId, Long milestoneId) throws Exception;

    Timestamp markMilestoneAsCompleted(Long contractId, Long milestoneId, MultipartFile file) throws Exception;

    void completeContract(Long contractId);
}
