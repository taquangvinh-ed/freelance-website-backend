package com.freelancemarketplace.backend.contract.application.service.imp;

import com.freelancemarketplace.backend.contract.application.service.ContractCrudService;
import com.freelancemarketplace.backend.contract.domain.model.ContractModel;
import com.freelancemarketplace.backend.contract.domain.model.MileStoneModel;
import com.freelancemarketplace.backend.contract.dto.ContractDTO;
import com.freelancemarketplace.backend.contract.dto.ContractResponseDTO;
import com.freelancemarketplace.backend.contract.dto.MileStoneDTO;
import com.freelancemarketplace.backend.contract.infrastructure.mapper.ContractMapper;
import com.freelancemarketplace.backend.contract.infrastructure.mapper.MileStoneMapper;
import com.freelancemarketplace.backend.contract.infrastructure.repository.ContractsRepository;
import com.freelancemarketplace.backend.exceptionHandling.ResourceNotFoundException;
import com.freelancemarketplace.backend.freelancer.infrastructure.repository.FreelancersRepository;
import com.freelancemarketplace.backend.freelancer.domain.model.FreelancerModel;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class ContractCrudServiceImp implements ContractCrudService {

    private final ContractsRepository contractsRepository;
    private final ContractMapper contractMapper;
    private final FreelancersRepository freelancersRepository;
    private final MileStoneMapper mileStoneMapper;

    @Override
    public ContractDTO updateContract(Long contractId, ContractDTO contractDTO) {
        ContractModel contract = contractsRepository.findById(contractId).orElseThrow(
                () -> new ResourceNotFoundException("Contract with id: " + contractId + " not found")
        );
        ContractModel updatedContract = contractMapper.partialUpdate(contractDTO, contract);
        ContractModel savedContract = contractsRepository.save(updatedContract);
        return contractMapper.toDto(savedContract);
    }

    @Override
    public void deleteContract(Long contractId) {
        if (!contractsRepository.existsById(contractId)) {
            throw new ResourceNotFoundException("Contract with id: " + contractId + " not found");
        }
        contractsRepository.deleteById(contractId);
    }

    @Override
    public List<ContractDTO> findAllContractByFreelancerId(Long freelancerId) {
        FreelancerModel freelancer = freelancersRepository.findById(freelancerId).orElseThrow(
                () -> new ResourceNotFoundException("Freelancer with id: " + freelancerId + " not found")
        );
        List<ContractModel> contracts = contractsRepository.findAllByFreelancer(freelancer);
        return contractMapper.toDTOs(contracts);
    }

    @Override
    public ContractResponseDTO getContractById(Long contractId) {
        ContractModel contract = contractsRepository.findById(contractId).orElseThrow(
                () -> new ResourceNotFoundException("Contract with id: " + contractId + " not found")
        );
        ContractResponseDTO contractResponseDTO = new ContractResponseDTO();
        contractResponseDTO.setContractId(contract.getContractId());
        contractResponseDTO.setTypes(contract.getTypes().toString());
        contractResponseDTO.setAmount(contract.getAmount());
        contractResponseDTO.setStartDate(contract.getStartDate());
        contractResponseDTO.setEndDate(contract.getEndDate());
        contractResponseDTO.setStatus(contract.getStatus().toString());
        contractResponseDTO.setProposalId(contract.getProposal().getProposalId());
        contractResponseDTO.setFreelancerName(contract.getFreelancer().getFirstName() + " " + contract.getFreelancer().getLastName());
        contractResponseDTO.setClientName(contract.getClient().getFirstName() + " " + contract.getClient().getLastName());

        List<MileStoneDTO> milestones = new ArrayList<>();
        contract.getMileStones().forEach(mileStoneModel -> {
            MileStoneDTO mileStoneDTO = mileStoneMapper.toDto(mileStoneModel);
            milestones.add(mileStoneDTO);
        });
        contractResponseDTO.setMileStones(milestones);

        return contractResponseDTO;
    }
}
