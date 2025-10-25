package com.freelancemarketplace.backend.service.imp;

import com.freelancemarketplace.backend.dto.MonthlyEarningsDTO;
import com.freelancemarketplace.backend.dto.OverallStatsDTO;
import com.freelancemarketplace.backend.dto.RecentClientDTO;
import com.freelancemarketplace.backend.dto.SkillDistributionDTO;
import com.freelancemarketplace.backend.enums.ContractStatus;
import com.freelancemarketplace.backend.enums.MileStoneStatus;
import com.freelancemarketplace.backend.exception.ResourceNotFoundException;
import com.freelancemarketplace.backend.model.*;
import com.freelancemarketplace.backend.repository.ContractsRepository;
import com.freelancemarketplace.backend.repository.FreelancersRepository;
import com.freelancemarketplace.backend.repository.TestimonialsRepository;
import com.freelancemarketplace.backend.service.DashboardFreelancerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.YearMonth;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
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
    public OverallStatsDTO getOverallStats(Long freelancerId) {
        FreelancerModel freelancer = freelancersRepository.findById(freelancerId).orElseThrow(
                () -> new ResourceNotFoundException("Freelancer with id: " + freelancerId + " not found")
        );

        List<ContractModel> contracts = contractsRepository.findAllByFreelancer(freelancer);

        double totalEarnings = contracts.stream()
                .flatMap(contract -> contract.getMileStones().stream())
                .filter(mileStone -> mileStone.getStatus() == MileStoneStatus.RELEASED)
                .mapToDouble(milestone -> milestone.getAmount().doubleValue())
                .sum();

        int activeProject = (int) contracts.stream()
                .filter(contract -> contract.getStatus() == ContractStatus.ACTIVE)
                .count();

        int pendingReview = (int) testimonialsRepository.countByFreelancer(freelancer);

        double hourlyRate = freelancer.getHourlyRate() != null ? freelancer.getHourlyRate() : 0.0;

        return new OverallStatsDTO(totalEarnings, activeProject, pendingReview, hourlyRate);
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

}
