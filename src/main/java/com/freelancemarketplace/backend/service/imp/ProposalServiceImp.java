package com.freelancemarketplace.backend.service.imp;

import com.freelancemarketplace.backend.dto.MileStoneDTO;
import com.freelancemarketplace.backend.dto.ProjectProposalDTO;
import com.freelancemarketplace.backend.dto.ProposalDTO;
import com.freelancemarketplace.backend.enums.*;
import com.freelancemarketplace.backend.exception.ProposalException;
import com.freelancemarketplace.backend.exception.ResourceNotFoundException;
import com.freelancemarketplace.backend.mapper.ContractMapper;
import com.freelancemarketplace.backend.mapper.MileStoneMapper;
import com.freelancemarketplace.backend.mapper.ProposalMapper;
import com.freelancemarketplace.backend.model.*;
import com.freelancemarketplace.backend.repository.*;
import com.freelancemarketplace.backend.service.ProposalService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProposalServiceImp implements ProposalService {

    private final ProposalsRepository proposalsRepository;
    private final ProposalMapper proposalMapper;
    private final FreelancersRepository freelancersRepository;
    private final TeamsRepository teamsRepository;
    private final ProjectsRepository projectsRepository;
    private final MileStoneMapper mileStoneMapper;
    private final ContractMapper contractMapper;
    private final ContractsRepository contractsRepository;


    @Override
    public ProposalDTO createProposal(Long freelancerId, ProposalDTO proposalDTO) {

        ProposalModel newProposal = proposalMapper.toEntity(proposalDTO);

        FreelancerModel freelancer = freelancersRepository.findById(freelancerId).orElseThrow(
                () -> new ResourceNotFoundException("Freelancer id: " + freelancerId + "inside proposal creating request create not found")
        );
        newProposal.setFreelancer(freelancer);
        newProposal.setStatus(ProposalStatus.PENDING);

        if (proposalDTO.getTeamId() != null) {
            TeamModel team = teamsRepository.findById(proposalDTO.getTeamId()).orElseThrow(
                    () -> new ResourceNotFoundException("Team id: " + proposalDTO.getTeamId() + "inside proposal creating request create not found")
            );
            newProposal.setTeam(team);
        }
        ProjectModel project = projectsRepository.findById(proposalDTO.getProjectId()).orElseThrow(
                () -> new ResourceNotFoundException("Project id: " + proposalDTO.getProjectId() + "inside proposal creating request create not found")
        );

        newProposal.setProject(project);


        if (proposalDTO.getMileStones() != null) {
            Set<MileStoneModel> milestones = new HashSet<>();
            proposalDTO.getMileStones().forEach(mileStoneDTO -> {
                MileStoneModel milestone = mileStoneMapper.toEntity(mileStoneDTO);
                milestone.setStatus(MileStoneStatus.PENDING);
                milestone.setProposal(newProposal);
                milestones.add(milestone);
            });
            newProposal.setMileStones(milestones);
        }

        ProposalModel savedProposal = proposalsRepository.save(newProposal);
        return proposalMapper.toDto(savedProposal);
    }

    @Override
    @Transactional
    public ProposalDTO updateProposal(Long proposalId, ProposalDTO proposalDTO) {

        // 1. Tải Proposal gốc (đối tượng Managed)
        ProposalModel existingProposal = proposalsRepository.findById(proposalId).orElseThrow(
                () -> new ResourceNotFoundException("Proposal with id: " + proposalId + " not found")
        );

        // 2. Cập nhật các trường Proposal chính (Áp dụng logic PATCH: chỉ cập nhật nếu giá trị DTO khác null)
        applyProposalPatch(existingProposal, proposalDTO);

        // 3. XỬ LÝ MERGE MILESTONE THỦ CÔNG (Nếu DTO có Milestone)
        if (proposalDTO.getMileStones() != null) {
            // ✅ GỌI HÀM VÀ TRUYỀN TẬP HỢP GỐC ĐỂ THAO TÁC TRỰC TIẾP TRÊN NÓ
            mergeMilestones(existingProposal, proposalDTO.getMileStones());
        }

        // 4. Lưu Proposal gốc (KHÔNG GỌI existingProposal.setMileStones()!)
        ProposalModel savedProposal = proposalsRepository.save(existingProposal);

        return proposalMapper.toDto(savedProposal);
    }


    @Override
    public void deleteProposal(Long proposalId) {
        ProposalModel proposal = proposalsRepository.findById(proposalId).orElseThrow(
                () -> new ResourceNotFoundException("Proposal with id: " + proposalId + " not found")
        );

        proposalsRepository.deleteById(proposalId);
    }

    @Override
    public ProposalDTO getProposalById(Long proposalId) {
        ProposalModel proposal = proposalsRepository.findById(proposalId).orElseThrow(
                () -> new ResourceNotFoundException("Proposal not found with id: " + proposalId)
        );
        return proposalMapper.toDto(proposal);
    }


    @Override
    public List<ProposalDTO> getAllProposalByFreelancerId(Long freelancerId) {
        FreelancerModel freelancer = freelancersRepository.findById(freelancerId).orElseThrow(
                () -> new ResourceNotFoundException("Freelancer id: " + freelancerId + "not found")
        );

        List<ProposalModel> proposals = proposalsRepository.findAllByFreelancer(freelancer);
        return proposalMapper.toDTOs(proposals);
    }

    @Override
    public List<ProposalDTO> getAllProposalByTeamId(Long teamId) {
        TeamModel team = teamsRepository.findById(teamId).orElseThrow(
                () -> new ResourceNotFoundException("team id: " + teamId + "not found")
        );

        List<ProposalModel> proposals = proposalsRepository.getAllByTeam(team);
        return proposalMapper.toDTOs(proposals);
    }


    @Override
    public Page<ProjectProposalDTO> getAllProposalByProject(Long projectId, Pageable pageable) {
        ProjectModel project = projectsRepository.findById(projectId).orElseThrow(
                () -> new ResourceNotFoundException("Project with id: " + projectId + " not found")
        );

        List<ProposalModel> proposals = proposalsRepository.findAllByProject(project);

        List<ProjectProposalDTO> projectProposals = proposals.stream().map((proposal) -> {
            FreelancerModel freelancer = proposal.getFreelancer();
            ProjectProposalDTO projectProposal = new ProjectProposalDTO();
            projectProposal.setId(proposal.getProposalId());
            projectProposal.setFreelancerId(freelancer.getFreelancerId());
            projectProposal.setImageUrl(freelancer.getAvatar());
            projectProposal.setFirstName(freelancer.getFirstName());
            projectProposal.setLastName(freelancer.getLastName());
//            projectProposal.setUsername(freelancer.getUser().getUsername())pr;
            projectProposal.setProposalDescription(proposal.getDescription());
            projectProposal.setAmount(proposal.getAmount());
            projectProposal.setDeliveryDays(projectProposal.getDeliveryDays());
            return projectProposal;
        }).collect(Collectors.toList());

        return new PageImpl<>(projectProposals, pageable, projectProposals.size());
    }

    @Override
    @Transactional
    public Long approveProposal(Long proposalId) {
        ProposalModel proposal = proposalsRepository.findById(proposalId).orElseThrow(
                () -> new ResourceNotFoundException("Proposal id: " + proposalId + "not found")
        );


        if (proposal.getStatus().equals(ProposalStatus.PENDING))
            proposal.setStatus(ProposalStatus.ACCEPTED);
        else
            throw new ProposalException("Cannot proposal cannot be accepted from status " + proposal.getStatus());

        ProposalModel savedProposal = proposalsRepository.save(proposal);
        proposalsRepository.rejectOtherProposalsInProject(
                savedProposal.getProject().getProjectId(),
                savedProposal.getProposalId()
        );

        ContractModel newContract = createContractFromProposal(proposal);
        ContractModel savedContract = contractsRepository.save(newContract);

        return savedContract.getContractId();
    }

    @Override
    public void rejectProposal(Long proposalId) {
        ProposalModel proposal = proposalsRepository.findById(proposalId).orElseThrow(
                () -> new ResourceNotFoundException("Proposal id: " + proposalId + "not found")
        );
        if (proposal.getStatus().equals(ProposalStatus.PENDING))
            proposal.setStatus(ProposalStatus.REJECTED);
        else
            throw new ProposalException("Cannot proposal cannot be rejected from status " + proposal.getStatus());
    }

    @Override
    public void withdrawProposal(Long proposalId) {
        ProposalModel proposal = proposalsRepository.findById(proposalId).orElseThrow(
                () -> new ResourceNotFoundException("Proposal id: " + proposalId + "not found")
        );
        if (proposal.getStatus().equals(ProposalStatus.PENDING))
            proposal.setStatus(ProposalStatus.WITHDRAWN);
        else
            throw new ProposalException(" Proposal cannot be withdraw from status " + proposal.getStatus());
    }

    @Override
    public ProposalDTO getProposalByFreelancerAndProject(Long freelancerId, Long projectId) {
        FreelancerModel freelancer = freelancersRepository.findById(freelancerId).orElseThrow(
                () -> new ResourceNotFoundException("Freelancer not found with id: " + freelancerId)
        );

        ProjectModel project = projectsRepository.findById(projectId).orElseThrow(
                () -> new ResourceNotFoundException("Freelancer not found with id: " + freelancerId)
        );

        ProposalModel proposal = proposalsRepository.findByFreelancerAndProject(freelancer, project);

        return proposalMapper.toDto(proposal);
    }

    private void applyProposalPatch(ProposalModel existingProposal, ProposalDTO proposalDTO) {
        if (proposalDTO.getName() != null) {
            existingProposal.setName(proposalDTO.getName());
        }
        if (proposalDTO.getDescription() != null) {
            existingProposal.setDescription(proposalDTO.getDescription());
        }
        if (proposalDTO.getCurrencyUnit() != null) {
            existingProposal.setCurrencyUnit(proposalDTO.getCurrencyUnit());
        }
        if (proposalDTO.getStatus() != null) {
            existingProposal.setStatus(ProposalStatus.valueOf(proposalDTO.getStatus()));
        }
        if (proposalDTO.getAmount() != null) {
            existingProposal.setAmount(proposalDTO.getAmount());
        }
        if (proposalDTO.getDeliveryDays() != null) {
            existingProposal.setDeliveryDays(proposalDTO.getDeliveryDays());
        }
        if (proposalDTO.getBudgetType() != null) {
            existingProposal.setBudgetType(BudgetTypes.valueOf(proposalDTO.getBudgetType()));
        }
        if (proposalDTO.getHourlyRate() != null) {
            existingProposal.setHourlyRate(proposalDTO.getHourlyRate());
        }
        if (proposalDTO.getEstimatedHours() != null) {
            existingProposal.setEstimatedHours(proposalDTO.getEstimatedHours());
        }
        // KHÔNG CẦN CẬP NHẬT proposalId, projectId, teamId
    }

    private void mergeMilestones(ProposalModel existingProposal, Set<MileStoneDTO> dtoMilestones) {

        Set<MileStoneModel> existingMilestones = existingProposal.getMileStones();

        // 1. CHUẨN BỊ DỮ LIỆU: Tạo Map Milestones cần được giữ lại/cập nhật (từ DTO)
        Map<Long, MileStoneModel> incomingMilestonesMap = new HashMap<>();

        for (MileStoneDTO dtoMilestone : dtoMilestones) {

            MileStoneModel milestoneToUpdate;

            if (dtoMilestone.getMileStoneId() != null) {
                // TRƯỜNG HỢP A: CẬP NHẬT MILESTONE HIỆN CÓ
                milestoneToUpdate = existingMilestones.stream()
                        .filter(m -> m.getMileStoneId().equals(dtoMilestone.getMileStoneId()))
                        .findFirst()
                        .orElse(null);

                if (milestoneToUpdate != null) {
                    applyMilestonePatch(milestoneToUpdate, dtoMilestone);
                    incomingMilestonesMap.put(milestoneToUpdate.getMileStoneId(), milestoneToUpdate);
                }
                // Nếu không tìm thấy (milestoneToUpdate == null), ta coi đó là lỗi logic và bỏ qua
            } else {
                // TRƯỜNG HỢP B: TẠO MỚI MILESTONE
                milestoneToUpdate = proposalMapper.toMileStoneEntity(dtoMilestone);

                // ✅ QUAN TRỌNG: Thêm trực tiếp vào tập hợp gốc (sẽ được thêm vào DB)
                existingMilestones.add(milestoneToUpdate);
                milestoneToUpdate.setProposal(existingProposal); // Thiết lập mối quan hệ

                // Nếu Milestone mới, nó sẽ không có ID nên không cần đưa vào map theo dõi ID
            }
        }

        // 2. XỬ LÝ XÓA: Lọc và xóa các Milestone bị thiếu trong request (orphans)

        // Tạo danh sách các Milestone cần xóa
        Set<MileStoneModel> milestonesToDelete = existingMilestones.stream()
                .filter(m -> m.getMileStoneId() != null && !incomingMilestonesMap.containsKey(m.getMileStoneId()))
                .collect(Collectors.toSet());

        // ✅ QUAN TRỌNG: Xóa trực tiếp khỏi tập hợp Managed (Hibernate sẽ xử lý DELETE)
        existingMilestones.removeAll(milestonesToDelete);

        // KHÔNG CÓ LỆNH RETURN Ở ĐÂY
    }

    private void applyMilestonePatch(MileStoneModel existingMilestone, MileStoneDTO dtoMilestone) {
        if (dtoMilestone.getName() != null) {
            existingMilestone.setName(dtoMilestone.getName());
        }
        if (dtoMilestone.getAmount() != null) {
            existingMilestone.setAmount(dtoMilestone.getAmount());
        }
        if (dtoMilestone.getCurrencyUnit() != null) {
            existingMilestone.setCurrencyUnit(dtoMilestone.getCurrencyUnit());
        }
        if (dtoMilestone.getDueDate() != null) {
            existingMilestone.setDueDate(dtoMilestone.getDueDate());
        }
        if (dtoMilestone.getDescription() != null) {
            existingMilestone.setDescription(dtoMilestone.getDescription());
        }

        // ⚠️ KHÔNG BAO GIỜ CẬP NHẬT CÁC TRƯỜNG ID LIÊN KẾT NHƯ proposalId, contractId, freelancerId
    }

    private ContractModel createContractFromProposal(ProposalModel proposalModel) {
        ContractModel contract = new ContractModel();
        contract.setAmount(proposalModel.getAmount().doubleValue());
        if (proposalModel.getBudgetType() == BudgetTypes.FIXED_PRICE)
            contract.setTypes(ContractTypes.FIXED_PRICE);
        else contract.setTypes(ContractTypes.HOURLY);
        contract.setStartDate(Timestamp.from(Instant.now()));
        if (proposalModel.getDeliveryDays() != null) {
            Timestamp startDate = Timestamp.from(Instant.now());
            LocalDateTime dateTime = startDate.toLocalDateTime();
            int deliveryDate = proposalModel.getDeliveryDays();
            dateTime = dateTime.plusDays(deliveryDate);
            Timestamp endDate = Timestamp.valueOf(dateTime);
            contract.setEndDate(endDate);
        }
        contract.setStatus(ContractStatus.ACTIVE);
        contract.setProposal(proposalModel);
        contract.setFreelancer(proposalModel.getFreelancer());
        contract.setClient(proposalModel.getProject().getClient());
        contract.setContractProject(proposalModel.getProject());
        contract.getMileStones().addAll(proposalModel.getMileStones());
        proposalModel.getMileStones().forEach(milestone -> {
            milestone.setContract(contract);
            milestone.setStatus(MileStoneStatus.ACCEPTED);
        });
        return contract;
    }

}
