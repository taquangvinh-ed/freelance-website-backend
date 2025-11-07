package com.freelancemarketplace.backend.service.imp;

import com.freelancemarketplace.backend.dto.*;
import com.freelancemarketplace.backend.enums.ContractStatus;
import com.freelancemarketplace.backend.enums.MileStoneStatus;
import com.freelancemarketplace.backend.exception.ResourceNotFoundException;
import com.freelancemarketplace.backend.mapper.ClientMapper;
import com.freelancemarketplace.backend.mapper.ContractMapper;
import com.freelancemarketplace.backend.mapper.MileStoneMapper;
import com.freelancemarketplace.backend.mapper.WeeklyReportMapper;
import com.freelancemarketplace.backend.model.*;
import com.freelancemarketplace.backend.repository.*;
import com.freelancemarketplace.backend.service.ContractLifeCycleService;
import com.freelancemarketplace.backend.service.ContractService;
import com.freelancemarketplace.backend.service.PaymentService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;
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
    private ClientsRepository clientsRepository;
    private ContractLifeCycleService contractLifeCycleService;
    private WeeklyReportModelRepository weeklyReportModelRepository;
private WeeklyReportMapper weeklyReportMapper;
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
        if (!contractsRepository.existsById(contractId))
            throw new ResourceNotFoundException("Contract with id: " + contractId + " not found");
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

    @Transactional
    @Override
    public MileStoneDTO processMilestonePayment(Long contractId, Long milestoneId) throws Exception {

        MileStoneModel mileStone = mileStoneModelRepository.findById(milestoneId).orElseThrow(
                () -> new ResourceNotFoundException("Milestone with id: " + milestoneId + " not found")
        );

        ContractModel contractModel = contractsRepository.findByIdWithClientAndUser(contractId).orElseThrow(
                () -> new ResourceNotFoundException("Contract with id: " + contractId + " not found")
        );

        ClientModel client = contractModel.getClient();
        System.out.println("ClientId: " + client.getClientId());
        if(client.getStripeCustomerId() == null){
            String clientName = client.getFirstName() + client.getLastName();
            String stripeCustomerId = paymentService.createStripeCustomer(client.getUser().getEmail(), clientName);
            client.setStripeCustomerId(stripeCustomerId);
            clientsRepository.save(client);
        }


        ClientDTO clientDTO = clientMapper.toDto(client);
        MileStoneDTO milestoneDTO = mileStoneMapper.toDto(mileStone);

        PaymentIntentResponse response = paymentService.createEscrowPayment(milestoneDTO, client.getClientId(), clientDTO, contractId);
        mileStone.setPaymentIntentId(response.getPaymentIntentId());
        mileStone.setStatus(MileStoneStatus.ESCROWED);
        MileStoneModel savedMilestone = mileStoneModelRepository.save(mileStone);
        MileStoneDTO result = mileStoneMapper.toDto(savedMilestone);
        result.setClientSecret(response.getClientSecret());
        return result;
    }


    @Override
    public Timestamp markMilestoneAsCompleted(Long contractId, Long milestoneId) throws Exception {
        ContractModel contract = contractsRepository.findById(contractId).orElseThrow(
                () -> new ResourceNotFoundException("Contract with id: " + contractId + " not found")
        );

        FreelancerModel freelancer = contract.getFreelancer();
        if(freelancer.getStripeCustomerId() == null){
            String freelancerName = freelancer.getFirstName()+freelancer.getLastName();
            String stripeCustomerId = paymentService.createStripeCustomer(freelancer.getUser().getEmail(), freelancerName);
            freelancer.setStripeCustomerId(stripeCustomerId);
            freelancersRepository.save(freelancer);
        }

        MileStoneModel milestone = contract.getMileStones().stream().filter(m -> m.getMileStoneId().equals(milestoneId))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Milestone with id: " + milestoneId + " not found"));
        milestone.setStatus(MileStoneStatus.COMPLETED);
        milestone.setCompletedAt(Timestamp.from(Instant.now()));
        contractsRepository.save(contract);
        return milestone.getCompletedAt();
    }


    @Override
    public void doneContractract(Long contractId){
        ContractModel contract = contractsRepository.findById(contractId).orElseThrow(
                () -> new ResourceNotFoundException("Contract with id: " + contractId + " not found")
        );
        if(contract.getStatus() != ContractStatus.ACTIVE){
            throw new IllegalStateException("Cannot complete contract. This contract is not currently ACTIVE.");        }
        contract.setStatus(ContractStatus.DONE);
        contractLifeCycleService.stopWeeklyReporting(contract.getContractId());
    }

    @Override
    public List<WeeklyReportDTO> getAllWeeklyReports(Long contractId){
        ContractModel contract = contractsRepository.findById(contractId).orElseThrow(
                () -> new ResourceNotFoundException("Contract with id: " + contractId + " not found")
        );
        List<WeeklyReportModel> reports = weeklyReportModelRepository.findAllByContract(contract);
        return weeklyReportMapper.toDTOs(reports);
    }

}
