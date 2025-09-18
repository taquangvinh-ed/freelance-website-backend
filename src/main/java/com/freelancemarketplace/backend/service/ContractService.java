package com.freelancemarketplace.backend.service;

import com.freelancemarketplace.backend.dto.ContractDTO;

import java.util.List;

public interface ContractService {

    ContractDTO createContract(ContractDTO contractDTO);

    ContractDTO updateContract(Long contractId, ContractDTO contractDTO);

    void deleteContract(Long contractId);

    List<ContractDTO> findAllContractByFreelancerId(Long freelancerId);
}
