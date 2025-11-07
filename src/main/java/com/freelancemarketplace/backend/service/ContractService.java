package com.freelancemarketplace.backend.service;

import com.freelancemarketplace.backend.dto.ContractDTO;
import com.freelancemarketplace.backend.dto.ContractResponseDTO;
import com.freelancemarketplace.backend.dto.MileStoneDTO;
import com.freelancemarketplace.backend.dto.WeeklyReportDTO;
import jakarta.transaction.Transactional;

import java.sql.Timestamp;
import java.util.List;

public interface ContractService {

    ContractDTO updateContract(Long contractId, ContractDTO contractDTO);

    void deleteContract(Long contractId);

    List<ContractDTO> findAllContractByFreelancerId(Long freelancerId);

    ContractResponseDTO getContractById(Long contractId);

    @Transactional
    MileStoneDTO processMilestonePayment(Long contractId, Long milestoneId) throws Exception;

    Timestamp markMilestoneAsCompleted(Long contractId, Long milestoneId) throws Exception;

    void doneContractract(Long contractId);

    List<WeeklyReportDTO> getAllWeeklyReports(Long contractId);
}
