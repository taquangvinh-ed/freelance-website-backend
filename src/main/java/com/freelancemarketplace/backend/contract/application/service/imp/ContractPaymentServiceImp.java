package com.freelancemarketplace.backend.contract.application.service.imp;

import com.freelancemarketplace.backend.client.domain.model.ClientModel;
import com.freelancemarketplace.backend.client.dto.ClientDTO;
import com.freelancemarketplace.backend.client.infrastructure.mapper.ClientMapper;
import com.freelancemarketplace.backend.client.infrastructure.repository.ClientsRepository;
import com.freelancemarketplace.backend.contract.application.service.ContractPaymentService;
import com.freelancemarketplace.backend.contract.domain.enums.ContractStatus;
import com.freelancemarketplace.backend.contract.domain.enums.MileStoneStatus;
import com.freelancemarketplace.backend.contract.domain.model.ContractModel;
import com.freelancemarketplace.backend.contract.domain.model.MileStoneModel;
import com.freelancemarketplace.backend.contract.dto.MileStoneDTO;
import com.freelancemarketplace.backend.contract.infrastructure.mapper.MileStoneMapper;
import com.freelancemarketplace.backend.contract.infrastructure.repository.ContractsRepository;
import com.freelancemarketplace.backend.contract.infrastructure.repository.MileStoneModelRepository;
import com.freelancemarketplace.backend.email.dto.PaymentIntentResponse;
import com.freelancemarketplace.backend.exceptionHandling.ApiException;
import com.freelancemarketplace.backend.exceptionHandling.ErrorCode;
import com.freelancemarketplace.backend.exceptionHandling.ResourceNotFoundException;
import com.freelancemarketplace.backend.freelancer.domain.model.FreelancerModel;
import com.freelancemarketplace.backend.freelancer.infrastructure.repository.FreelancersRepository;
import com.freelancemarketplace.backend.payment.application.service.PaymentService;
import com.freelancemarketplace.backend.project.application.service.CloudinaryService;
import com.freelancemarketplace.backend.contract.application.service.ContractLifeCycleService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.sql.Timestamp;
import java.time.Instant;

@Service
@AllArgsConstructor
public class ContractPaymentServiceImp implements ContractPaymentService {

    private final ContractsRepository contractsRepository;
    private final MileStoneModelRepository mileStoneModelRepository;
    private final ClientsRepository clientsRepository;
    private final FreelancersRepository freelancersRepository;
    private final PaymentService paymentService;
    private final MileStoneMapper mileStoneMapper;
    private final ClientMapper clientMapper;
    private final CloudinaryService cloudinaryService;
    private final ContractLifeCycleService contractLifeCycleService;

    @Override
    @Transactional
    public MileStoneDTO processMilestonePayment(Long contractId, Long milestoneId) throws Exception {
        MileStoneModel mileStone = mileStoneModelRepository.findById(milestoneId).orElseThrow(
                () -> new ResourceNotFoundException("Milestone with id: " + milestoneId + " not found")
        );

        ContractModel contractModel = contractsRepository.findByIdWithClientAndUser(contractId).orElseThrow(
                () -> new ResourceNotFoundException("Contract with id: " + contractId + " not found")
        );

        ClientModel client = contractModel.getClient();
        if (client.getStripeCustomerId() == null) {
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
    public Timestamp markMilestoneAsCompleted(Long contractId, Long milestoneId, MultipartFile file) throws Exception {
        ContractModel contract = contractsRepository.findById(contractId).orElseThrow(
                () -> new ResourceNotFoundException("Contract with id: " + contractId + " not found")
        );

        FreelancerModel freelancer = contract.getFreelancer();
        if (freelancer.getStripeCustomerId() == null) {
            String freelancerName = freelancer.getFirstName() + freelancer.getLastName();
            String stripeCustomerId = paymentService.createStripeCustomer(freelancer.getUser().getEmail(), freelancerName);
            freelancer.setStripeCustomerId(stripeCustomerId);
            freelancersRepository.save(freelancer);
        }

        MileStoneModel milestone = contract.getMileStones().stream().filter(m -> m.getMileStoneId().equals(milestoneId))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Milestone with id: " + milestoneId + " not found"));
        milestone.setStatus(MileStoneStatus.COMPLETED);
        milestone.setCompletedAt(Timestamp.from(Instant.now()));
        String fileUrl = null;
        String fileName = null;
        if (file != null && !file.isEmpty()) {
            try {
                fileUrl = cloudinaryService.uploadFileMilestone(file);
                fileName = file.getOriginalFilename();
            } catch (IOException e) {
                throw new FileUploadException("Upload file đính kèm thất bại: " + e.getMessage(), e);
            } catch (Exception e) {
                throw new ApiException(HttpStatus.BAD_GATEWAY, ErrorCode.EXTERNAL_SERVICE_ERROR,
                        "Lỗi không xác định khi upload file milestone", e);
            }
        }
        milestone.setFileUrl(fileUrl);
        milestone.setFileName(fileName);
        contractsRepository.save(contract);
        return milestone.getCompletedAt();
    }

    @Override
    public void completeContract(Long contractId) {
        ContractModel contract = contractsRepository.findById(contractId).orElseThrow(
                () -> new ResourceNotFoundException("Contract with id: " + contractId + " not found")
        );
        if (contract.getStatus() != ContractStatus.ACTIVE) {
            throw new ApiException(HttpStatus.BAD_REQUEST, ErrorCode.INVALID_OPERATION,
                    "Cannot complete contract. This contract is not currently ACTIVE.");
        }
        contract.setStatus(ContractStatus.DONE);
        contractsRepository.save(contract);
        contractLifeCycleService.stopWeeklyReporting(contract.getContractId());
    }
}
