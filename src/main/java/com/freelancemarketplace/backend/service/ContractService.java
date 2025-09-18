package com.freelancemarketplace.backend.service;

import com.freelancemarketplace.backend.dto.ContractDTO;

public interface ContractService {

    ContractDTO createContract(ContractDTO contractDTO);

    ContractDTO updateContract(Long contractId, ContractDTO contractDTO);

    void deleteContract(Long contractId);
}
