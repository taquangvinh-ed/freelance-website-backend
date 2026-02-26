package com.freelancemarketplace.backend.service.imp;

import com.freelancemarketplace.backend.dto.*;
import com.freelancemarketplace.backend.enums.ContractStatus;
import com.freelancemarketplace.backend.enums.MileStoneStatus;
import com.freelancemarketplace.backend.exception.ResourceNotFoundException;
import com.freelancemarketplace.backend.model.*;
import com.freelancemarketplace.backend.repository.ContractsRepository;
import com.freelancemarketplace.backend.repository.FreelancersRepository;
import com.freelancemarketplace.backend.repository.TestimonialsRepository;
import com.freelancemarketplace.backend.service.DashboardFreelancerService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.YearMonth;
import java.util.*;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class DashboardFreelancerServiceImp implements DashboardFreelancerService {

    private final ContractsRepository contractsRepository;
    private final FreelancersRepository freelancersRepository;
    private final TestimonialsRepository testimonialsRepository;



    @Override
    public List<MonthlyEarningsDTO> getMonthlyEarnings(Long freelancerId, int months) {

        FreelancerModel freelancer = freelancersRepository.findById(freelancerId).orElseThrow(
                () -> new ResourceNotFoundException("Freelancer with id: " + freelancerId + " not found")
        );

        List<ContractModel> contracts = contractsRepository.findAllByFreelancer(freelancer);

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, -(months - 1));
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);

        Timestamp startDate = new Timestamp(calendar.getTimeInMillis());

        Map<YearMonth, Double> earningsByMonth = contracts.stream()
                .flatMap(contract -> contract.getMileStones().stream())
                .filter(milestone -> milestone.getStatus() == MileStoneStatus.RELEASED && milestone.getCompletedAt() != null && milestone.getCompletedAt().after(startDate))
                .collect(Collectors.groupingBy(
                        milestone -> {
                            Calendar cal = Calendar.getInstance();
                            cal.setTime(milestone.getCompletedAt());
                            return YearMonth.of(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH) + 1);
                        },
                        Collectors.summingDouble(milestone -> milestone.getAmount().doubleValue())
                ));

        List<MonthlyEarningsDTO> result = new ArrayList<>();
        Calendar current = Calendar.getInstance();
        for (int i = 0; i < months; i++) {
            YearMonth yearMonth = YearMonth.of(current.get(Calendar.YEAR), current.get(Calendar.MONTH) + 1);
            String mothLabel = "T" + yearMonth.getMonthValue();
            double earnings = earningsByMonth.getOrDefault(yearMonth, 0.0);
            result.add(new MonthlyEarningsDTO(mothLabel, earnings));
            current.add(Calendar.MONTH, -1);
        }

        Collections.reverse(result);

        return result;
    }

    @Override
    @Transactional
    public OverallStatsDTO getOverallStats(Long freelancerId) {
        FreelancerModel freelancer = freelancersRepository.findById(freelancerId).orElseThrow(
                () -> new ResourceNotFoundException("Freelancer with id: " + freelancerId + " not found")
        );

        List<ContractModel> contracts = contractsRepository.findAllByFreelancer(freelancer);
        log.info("Total contracts for freelancer {}: {}", freelancerId, contracts.size());

        double totalEarnings = contracts.stream()
                .flatMap(contract -> {
                    // Null safety check for milestones
                    Set<MileStoneModel> milestones = contract.getMileStones();
                    if (milestones == null) {
                        log.warn("Contract {} has null milestones", contract.getContractId());
                        return java.util.stream.Stream.empty();
                    }
                    return milestones.stream();
                })
                .filter(mileStone -> mileStone.getStatus() == MileStoneStatus.RELEASED)
                .mapToDouble(milestone -> {
                    double amount = milestone.getAmount() != null ? milestone.getAmount().doubleValue() : 0.0;
                    log.debug("Found RELEASED milestone: {} with amount: {}", milestone.getMileStoneId(), amount);
                    return amount;
                })
                .sum();

        log.info("Total earnings for freelancer {}: {}", freelancerId, totalEarnings);

        int activeProject = (int) contracts.stream()
                .filter(contract -> contract.getStatus() == ContractStatus.ACTIVE)
                .count();

        log.info("Active projects for freelancer {}: {}", freelancerId, activeProject);

        int pendingReview = (int) testimonialsRepository.countByFreelancer(freelancer);
        log.info("Pending reviews for freelancer {}: {}", freelancerId, pendingReview);

        double hourlyRate = freelancer.getHourlyRate() != null ? freelancer.getHourlyRate() : 0.0;
        log.info("Hourly rate for freelancer {}: {}", freelancerId, hourlyRate);

        OverallStatsDTO result = new OverallStatsDTO(totalEarnings, activeProject, pendingReview, hourlyRate);
        log.info("Overall stats: earnings={}, activeProjects={}, pendingReview={}, hourlyRate={}",
                totalEarnings, activeProject, pendingReview, hourlyRate);

        return result;
    }


    @Override
    public List<SkillDistributionDTO> getSkillDistribution(Long freelancerId) {
        FreelancerModel freelancer = freelancersRepository.findById(freelancerId).orElseThrow(
                () -> new ResourceNotFoundException("Freelancer with id: " + freelancerId + " not found")
        );

        List<SkillModel> freelancerSkills = freelancer.getSkills().stream().toList();

        List<ContractModel> contracts = contractsRepository.findAllByFreelancer(freelancer);

        List<ProjectModel> projects = contracts.stream().filter(contractModel -> contractModel.getStatus() == ContractStatus.ACTIVE || contractModel.getStatus() == ContractStatus.COMPLETED)
                .map(ContractModel::getContractProject)
                .filter(Objects::nonNull)
                .toList();

        Map<String, Double> skillDistribution = new HashMap<>();
        if (projects.isEmpty()) {
            double defaultValue = 100.0 / freelancerSkills.size();
            for (SkillModel skill : freelancerSkills) {
                skillDistribution.put(skill.getName(), defaultValue);
            }
        } else {
            Map<String, Long> skillCounts = new HashMap<>();
            for (ProjectModel project : projects) {
                List<SkillModel> projectSkills = project.getSkills().stream().toList();
                for (SkillModel skillModel : projectSkills) {
                    if (freelancerSkills.stream().anyMatch(fs -> fs.getName().equals(skillModel.getName()))) {
                        skillCounts.merge(skillModel.getName(), 1L, Long::sum);
                    }
                }
            }

            long totalProjects = skillCounts.values().stream().mapToLong(Long::longValue).sum();
            if (totalProjects == 0) {
                double defaultValue = 100.0 / freelancerSkills.size();
                for (SkillModel skill : freelancerSkills) {
                    skillDistribution.put(skill.getName(), defaultValue);
                }
            } else {
                for (SkillModel skill : freelancerSkills) {
                    long count = skillCounts.getOrDefault(skill.getName(), 0L);
                    double percentage = (count * 100.0) / totalProjects;
                    skillDistribution.put(skill.getName(), percentage);
                }
            }
        }
        return skillDistribution.entrySet().stream()
                .map(entry -> new SkillDistributionDTO(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());
    }

    @Override
    public List<RecentClientDTO> getRencentClients(Long freelancerId) {
        FreelancerModel freelancer = freelancersRepository.findById(freelancerId).orElseThrow(
                () -> new ResourceNotFoundException("Freelancer with id: " + freelancerId + " not found")
        );

        List<ContractModel> contracts = contractsRepository.findAllByFreelancer(freelancer);

        Map<Long, RecentClientDTO> clientMap = contracts.stream()
                .filter(contract -> contract.getStatus() == ContractStatus.ACTIVE || contract.getStatus() == ContractStatus.COMPLETED)
                .collect(Collectors.toMap(
                        contract -> contract.getClient().getClientId(),
                        contract -> {
                            ClientModel client = contract.getClient();
                            String clientName = (client.getFirstName() != null && client.getLastName() != null)
                                    ? client.getFirstName() + " " + client.getLastName()
                                    :  "Unknown Client";
                            String projectName = contract.getContractProject() != null
                                    ? contract.getContractProject().getTitle() : "Unknown Project";
                            return new RecentClientDTO(
                                    client.getClientId(),
                                    client.getAvatar(),
                                    clientName,
                                    projectName,
                                    contract.getCreatedAt()

                            );
                        },
                        (existing, replacement) -> {
                            // Giữ contract có createdAt mới nhất
                            return existing.getLastWorked().after(replacement.getLastWorked()) ? existing : replacement;
                        }
                ));

        // Sắp xếp theo lastWorked (giảm dần) và giới hạn số lượng
        return clientMap.values().stream()
                .sorted(Comparator.comparing(RecentClientDTO::getLastWorked, Comparator.reverseOrder()))
                .limit(5) // Mặc định 5 khách hàng
                .collect(Collectors.toList());

    }

    @Override
    @Transactional
    public List<DashboardProjectResponse> getAllActivedProjects(Long freelancerId){
        FreelancerModel freelancer = freelancersRepository.findById(freelancerId).orElseThrow(
                () -> new ResourceNotFoundException("Freelancer with id: " + freelancerId + " not found")
        );

        List<ContractModel> contracts = contractsRepository.findAllByFreelancer(freelancer);
        log.info("Total contracts for freelancer {}: {}", freelancerId, contracts.size());

        List<ContractModel> activeContracts = contracts.stream()
                .filter(contract -> contract.getStatus() == ContractStatus.ACTIVE)
                .toList();

        log.info("Active contracts for freelancer {}: {}", freelancerId, activeContracts.size());

        return activeContracts.stream()
                .map(contractModel -> {
                    // Null safety checks for lazy-loaded relationships
                    if (contractModel.getContractProject() == null) {
                        log.warn("Contract {} has no associated project", contractModel.getContractId());
                        return null;
                    }
                    if (contractModel.getClient() == null) {
                        log.warn("Contract {} has no associated client", contractModel.getContractId());
                        return null;
                    }

                    DashboardProjectResponse activedProject = new DashboardProjectResponse();
                    activedProject.setProjectId(contractModel.getContractProject().getProjectId());
                    activedProject.setContractId(contractModel.getContractId());
                    activedProject.setProjectName(contractModel.getContractProject().getTitle());

                    String clientName = "Unknown Client";
                    if (contractModel.getClient().getFirstName() != null && contractModel.getClient().getLastName() != null) {
                        clientName = contractModel.getClient().getFirstName() + " " + contractModel.getClient().getLastName();
                    } else if (contractModel.getClient().getFirstName() != null) {
                        clientName = contractModel.getClient().getFirstName();
                    }
                    activedProject.setClientName(clientName);

                    if (contractModel.getEndDate() != null) {
                        activedProject.setDeadline(contractModel.getEndDate().toString());
                    }
                    return activedProject;
                })
                .filter(Objects::nonNull)
                .toList();
    }

    @Override
    @Transactional
    public List<DashboardProjectResponse> getAllCancelledProjects(Long freelancerId){
        FreelancerModel freelancer = freelancersRepository.findById(freelancerId).orElseThrow(
                () -> new ResourceNotFoundException("Freelancer with id: " + freelancerId + " not found")
        );

        List<ContractModel> contracts = contractsRepository.findAllByFreelancer(freelancer);
        log.info("Total contracts for freelancer {}: {}", freelancerId, contracts.size());

        List<ContractModel> cancelledContracts = contracts.stream()
                .filter(contract -> contract.getStatus() == ContractStatus.CANCELLED)
                .toList();

        log.info("Cancelled contracts for freelancer {}: {}", freelancerId, cancelledContracts.size());

        return cancelledContracts.stream()
                .map(contractModel -> {
                    // Null safety checks
                    if (contractModel.getContractProject() == null) {
                        log.warn("Cancelled Contract {} has no associated project", contractModel.getContractId());
                        return null;
                    }
                    if (contractModel.getClient() == null) {
                        log.warn("Cancelled Contract {} has no associated client", contractModel.getContractId());
                        return null;
                    }

                    DashboardProjectResponse cancelledProject = new DashboardProjectResponse();
                    cancelledProject.setProjectId(contractModel.getContractProject().getProjectId());
                    cancelledProject.setContractId(contractModel.getContractId());
                    cancelledProject.setProjectName(contractModel.getContractProject().getTitle());

                    String clientName = "Unknown Client";
                    if (contractModel.getClient().getFirstName() != null && contractModel.getClient().getLastName() != null) {
                        clientName = contractModel.getClient().getFirstName() + " " + contractModel.getClient().getLastName();
                    } else if (contractModel.getClient().getFirstName() != null) {
                        clientName = contractModel.getClient().getFirstName();
                    }
                    cancelledProject.setClientName(clientName);
                    return cancelledProject;
                })
                .filter(Objects::nonNull)
                .toList();
    }

    @Override
    @Transactional
    public List<DashboardProjectResponse> getAllCompletedProjects(Long freelancerId){
        FreelancerModel freelancer = freelancersRepository.findById(freelancerId).orElseThrow(
                () -> new ResourceNotFoundException("Freelancer with id: " + freelancerId + " not found")
        );

        List<ContractModel> contracts = contractsRepository.findAllByFreelancer(freelancer);
        log.info("Total contracts for freelancer {}: {}", freelancerId, contracts.size());

        List<ContractModel> completedContracts = contracts.stream()
                .filter(contract -> contract.getStatus() == ContractStatus.COMPLETED)
                .toList();

        log.info("Completed contracts for freelancer {}: {}", freelancerId, completedContracts.size());

        return completedContracts.stream()
                .map(contractModel -> {
                    // Null safety checks
                    if (contractModel.getContractProject() == null) {
                        log.warn("Completed Contract {} has no associated project", contractModel.getContractId());
                        return null;
                    }
                    if (contractModel.getClient() == null) {
                        log.warn("Completed Contract {} has no associated client", contractModel.getContractId());
                        return null;
                    }

                    DashboardProjectResponse completedProject = new DashboardProjectResponse();
                    completedProject.setProjectId(contractModel.getContractProject().getProjectId());
                    completedProject.setContractId(contractModel.getContractId());
                    completedProject.setProjectName(contractModel.getContractProject().getTitle());

                    String clientName = "Unknown Client";
                    if (contractModel.getClient().getFirstName() != null && contractModel.getClient().getLastName() != null) {
                        clientName = contractModel.getClient().getFirstName() + " " + contractModel.getClient().getLastName();
                    } else if (contractModel.getClient().getFirstName() != null) {
                        clientName = contractModel.getClient().getFirstName();
                    }
                    completedProject.setClientName(clientName);
                    return completedProject;
                })
                .filter(Objects::nonNull)
                .toList();
    }



}
