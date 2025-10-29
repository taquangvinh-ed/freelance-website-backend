package com.freelancemarketplace.backend.service.imp;

import com.freelancemarketplace.backend.dto.ClientDashboardStatsDTO;
import com.freelancemarketplace.backend.dto.ProjectTrackingDTO;
import com.freelancemarketplace.backend.enums.ContractStatus;
import com.freelancemarketplace.backend.enums.ContractTypes;
import com.freelancemarketplace.backend.enums.MileStoneStatus;
import com.freelancemarketplace.backend.exception.ResourceNotFoundException;
import com.freelancemarketplace.backend.model.ClientModel;
import com.freelancemarketplace.backend.model.ContractModel;
import com.freelancemarketplace.backend.model.FreelancerModel;
import com.freelancemarketplace.backend.model.MileStoneModel;
import com.freelancemarketplace.backend.repository.ClientsRepository;
import com.freelancemarketplace.backend.repository.ContractsRepository;
import com.freelancemarketplace.backend.service.DashboardClientService;
import com.freelancemarketplace.backend.service.ProgressCalculationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DashboardClientServiceImp implements DashboardClientService {

    private final ContractsRepository contractsRepository;
    private final ClientsRepository clientsRepository;
    private final ProgressCalculationService progressService;

    @Override
    public ClientDashboardStatsDTO getStats(Long clientId) {

        ClientModel client = clientsRepository.findById(clientId).orElseThrow(
                ()-> new ResourceNotFoundException("Client with id: " + clientId + " not found")
        );

        List<ContractModel> contracts = contractsRepository.findAllByClient(client);

        if(contracts.isEmpty())
            return new ClientDashboardStatsDTO();

        ClientDashboardStatsDTO result = new ClientDashboardStatsDTO();

        long totalProjects = contracts.stream().count();
        result.setTotalProjects((int)totalProjects);

        long activeProjects = contracts.stream().filter(contract -> contract.getStatus() == ContractStatus.ACTIVE).count();
        result.setActiveProjects((int)activeProjects);

        long pendingPayment = contracts.stream().filter(contractModel -> contractModel.getStatus() == ContractStatus.ACTIVE)
                .filter(contractModel -> contractModel.getTypes() == ContractTypes.FIXED_PRICE)
                .mapToLong(contract-> contract.getMileStones().stream()
                        .filter(mileStoneModel -> mileStoneModel.getStatus() == MileStoneStatus.ACCEPTED).count()).sum();
        result.setPendingPayments((int)pendingPayment);

        Optional<BigDecimal> totalSpent = contracts.stream()
                .filter(contractModel -> contractModel.getStatus() == ContractStatus.ACTIVE || contractModel.getStatus()==ContractStatus.COMPLETED)
                .filter(contractModel -> contractModel.getTypes() == ContractTypes.FIXED_PRICE)
                .flatMap(contract -> contract.getMileStones().stream()
                        .filter(mileStoneModel -> mileStoneModel.getStatus() == MileStoneStatus.ESCROWED || mileStoneModel.getStatus() == MileStoneStatus.COMPLETED)
                        .map(MileStoneModel::getAmount)

                ).reduce(BigDecimal::add);
        BigDecimal finalTotalSpent = totalSpent.orElse(BigDecimal.ZERO);
        result.setTotalSpent(finalTotalSpent);
        return result;
    }


    @Override
    public List<ProjectTrackingDTO> getClientActiveProjects(Long clientId) {

        // 1. Lấy danh sách hợp đồng ACTIVE của Client
        // Giả định bạn có phương thức tìm kiếm hợp đồng theo Client ID và Status

        ClientModel client = clientsRepository.findById(clientId).orElseThrow(
                ()-> new ResourceNotFoundException("Client with id: " + clientId + " not found")
        );

        List<ContractModel> contracts = contractsRepository.findAllByClient(client);
        List<ContractModel> activeContracts = contracts.stream().filter(contractModel -> contractModel.getStatus() == ContractStatus.ACTIVE).toList();

        // 2. Map từ ContractModel sang ProjectTrackingDTO
        return activeContracts.stream()
                .map(contract -> {
                    ProjectTrackingDTO dto = new ProjectTrackingDTO();
                    dto.setContractId(contract.getContractId());
                    dto.setProjectTitle(contract.getContractProject().getTitle());
                    dto.setStatus(contract.getStatus().name());
                    dto.setDeadline(contract.getEndDate());

                    // Lấy thông tin Freelancer (Giả định ContractModel có trường FreelancerModel)
                    FreelancerModel freelancer = contract.getFreelancer();
                    if (freelancer != null) {
                        dto.setFreelancerName(freelancer.getFirstName() + " " + freelancer.getLastName());
                        dto.setFreelancerProfileUrl("/freelancers/" + freelancer.getFreelancerId());
                    }

                    // 3. Tính toán Tiến độ (Logic phức tạp nhất)
                    // Hàm này sẽ tính tiến độ dựa trên Milestones (FIXED) hoặc TimeSheets (HOURLY)
                    double progress = progressService.calculateProgress(contract);
                    dto.setProgressPercentage(progress);

                    return dto;
                })
                .collect(Collectors.toList());
    }

}
