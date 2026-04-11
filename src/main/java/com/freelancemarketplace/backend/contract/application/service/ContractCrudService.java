package com.freelancemarketplace.backend.contract.application.service;

import com.freelancemarketplace.backend.contract.dto.ContractDTO;
import com.freelancemarketplace.backend.contract.dto.ContractResponseDTO;

import java.util.List;

public interface ContractCrudService {

    ContractDTO updateContract(Long contractId, ContractDTO contractDTO);

    void deleteContract(Long contractId);

    List<ContractDTO> findAllContractByFreelancerId(Long freelancerId);

    ContractResponseDTO getContractById(Long contractId);
}
