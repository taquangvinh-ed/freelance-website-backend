package com.freelancemarketplace.backend.service;

import com.freelancemarketplace.backend.dto.ContractDTO;
import com.freelancemarketplace.backend.dto.ContractResponseDTO;
import com.freelancemarketplace.backend.dto.MileStoneDTO;
import jakarta.transaction.Transactional;

import java.util.List;

public interface ContractService {

    ContractDTO updateContract(Long contractId, ContractDTO contractDTO);

    void deleteContract(Long contractId);

    List<ContractDTO> findAllContractByFreelancerId(Long freelancerId);

    ContractResponseDTO getContractById(Long contractId);

    @Transactional
    MileStoneDTO processMilestonePayment(Long contractId, Long milestoneId) throws Exception;
}
