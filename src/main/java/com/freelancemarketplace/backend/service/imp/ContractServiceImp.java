package com.freelancemarketplace.backend.service.imp;

import com.freelancemarketplace.backend.dto.*;
import com.freelancemarketplace.backend.enums.MileStoneStatus;
import com.freelancemarketplace.backend.exception.ResourceNotFoundException;
import com.freelancemarketplace.backend.mapper.ClientMapper;
import com.freelancemarketplace.backend.mapper.ContractMapper;
import com.freelancemarketplace.backend.mapper.MileStoneMapper;
import com.freelancemarketplace.backend.model.*;
import com.freelancemarketplace.backend.repository.*;
import com.freelancemarketplace.backend.service.ContractService;
import com.freelancemarketplace.backend.service.PaymentService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class ContractServiceImp implements ContractService {


    private ContractsRepository contractsRepository;
    private ContractMapper contractMapper;
    private FreelancersRepository freelancersRepository;
    private MileStoneMapper mileStoneMapper;
    private MileStoneModelRepository mileStoneModelRepository;
    private ClientMapper clientMapper;
    private PaymentService paymentService;

    @Override
    public ContractDTO updateContract(Long contractId, ContractDTO contractDTO) {

        ContractModel contract = contractsRepository.findById(contractId).orElseThrow(
                () -> new ResourceNotFoundException("Contract with id: " +contractId+ " not found")
        );
        ContractModel updatedContract = contractMapper.partialUpdate(contractDTO, contract);

        ContractModel savedContract = contractsRepository.save(updatedContract);

        return contractMapper.toDto(savedContract);
    }

    @Override
    public void deleteContract(Long contractId) {
        if(!contractsRepository.existsById(contractId))
            throw new ResourceNotFoundException("Contract with id: " +contractId+ " not found");
        contractsRepository.deleteById(contractId);
    }

    @Override
    public List<ContractDTO> findAllContractByFreelancerId(Long freelancerId) {

        FreelancerModel freelancer = freelancersRepository.findById(freelancerId).orElseThrow(
                ()-> new ResourceNotFoundException("Freelancer with id: " +freelancerId+ " not found")
        );

        List<ContractModel> contracts = contractsRepository.findAllByFreelancer(freelancer);

        return contractMapper.toDTOs(contracts);
    }

    @Override
    public ContractResponseDTO getContractById(Long contractId){
        ContractModel contract = contractsRepository.findById(contractId).orElseThrow(
                ()-> new ResourceNotFoundException("Contract with id: " +contractId+ " not found")
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
        contractResponseDTO.setClientName(contract.getClient().getFirstName()+" " + contract.getClient().getLastName());

        List<MileStoneDTO> milestones = new ArrayList<>();

        contract.getMileStones().forEach(mileStoneModel -> {
            MileStoneDTO mileStoneDTO = mileStoneMapper.toDto(mileStoneModel);
            milestones.add(mileStoneDTO);
        });
        contractResponseDTO.setMileStones(milestones);

        return contractResponseDTO;
    }

    @Transactional
    @Override
    public MileStoneDTO processMilestonePayment(Long contractId, Long milestoneId) throws Exception {
        MileStoneModel mileStone = mileStoneModelRepository.findById(milestoneId).orElseThrow(
                ()-> new ResourceNotFoundException("Milestone with id: " + milestoneId + " not found")
        );

        ContractModel contractModel = contractsRepository.findById(contractId).orElseThrow(
                ()-> new ResourceNotFoundException("Contract with id: " + contractId + " not found")
        );

        ClientModel client = contractModel.getClient();

        ClientDTO clientDTO = clientMapper.toDto(client);
        MileStoneDTO milestoneDTO = mileStoneMapper.toDto(mileStone);

        PaymentIntentResponse response = paymentService.createEscrowPayment(milestoneDTO, clientDTO);
        mileStone.setPaymentIntentId(response.getPaymentIntentId());
        mileStone.setStatus(MileStoneStatus.ESCROWED);
        MileStoneModel savedMilestone = mileStoneModelRepository.save(mileStone);
        MileStoneDTO result =  mileStoneMapper.toDto(savedMilestone);
        result.setClientSecret(response.getClientSecret());
        return result;
    }


}
