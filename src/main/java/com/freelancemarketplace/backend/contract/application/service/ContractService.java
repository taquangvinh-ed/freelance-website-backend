package com.freelancemarketplace.backend.contract.application.service;

import com.freelancemarketplace.backend.contract.dto.ContractDTO;
import com.freelancemarketplace.backend.contract.dto.ContractResponseDTO;
import com.freelancemarketplace.backend.contract.dto.MileStoneDTO;
import com.freelancemarketplace.backend.contract.dto.WeeklyReportDTO;
import jakarta.transaction.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.sql.Timestamp;
import java.util.List;

public interface ContractService {

    ContractDTO updateContract(Long contractId, ContractDTO contractDTO);

    void deleteContract(Long contractId);

    List<ContractDTO> findAllContractByFreelancerId(Long freelancerId);

    ContractResponseDTO getContractById(Long contractId);

    @Transactional
    MileStoneDTO processMilestonePayment(Long contractId, Long milestoneId) throws Exception;

    Timestamp markMilestoneAsCompleted(Long contractId, Long milestoneId, MultipartFile file) throws Exception;

    void doneContractract(Long contractId);

    List<WeeklyReportDTO> getAllWeeklyReports(Long contractId);
}
