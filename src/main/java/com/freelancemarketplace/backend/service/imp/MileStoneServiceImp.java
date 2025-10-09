package com.freelancemarketplace.backend.service.imp;

import com.freelancemarketplace.backend.dto.MileStoneDTO;
import com.freelancemarketplace.backend.enums.MileStoneStatus;
import com.freelancemarketplace.backend.exception.ResourceNotFoundException;
import com.freelancemarketplace.backend.mapper.MileStoneMapper;
import com.freelancemarketplace.backend.model.ContractModel;
import com.freelancemarketplace.backend.model.MileStoneModel;
import com.freelancemarketplace.backend.model.ProposalModel;
import com.freelancemarketplace.backend.repository.ContractsRepository;
import com.freelancemarketplace.backend.repository.MileStoneModelRepository;
import com.freelancemarketplace.backend.repository.ProposalsRepository;
import com.freelancemarketplace.backend.service.MileStoneService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class MileStoneServiceImp implements MileStoneService {

    private MileStoneModelRepository mileStoneModelRepository;
    private MileStoneMapper mileStoneMapper;
    private ContractsRepository contractsRepository;
    private ProposalsRepository proposalsRepository;


    @Override
    public MileStoneDTO createMileStone(MileStoneDTO mileStoneDTO) {

        MileStoneModel newMileStone = mileStoneMapper.toEntity(mileStoneDTO);

        ContractModel contract = contractsRepository.findById(mileStoneDTO.getContractId()).orElseThrow(
                ()->new ResourceNotFoundException("Contract with Id: " + mileStoneDTO.getContractId() + " not found")
        );

        ProposalModel proposal = proposalsRepository.findById(mileStoneDTO.getProposalId()).orElseThrow(
                ()->new ResourceNotFoundException("Contract with Id: " + mileStoneDTO.getContractId() + " not found")
        );
        newMileStone.setProposal(proposal);

        if (newMileStone.getStatus() == null) {
            newMileStone.setStatus(MileStoneStatus.PENDING);
        }
        MileStoneModel savedMileStone = mileStoneModelRepository.save(newMileStone);
        return mileStoneMapper.toDto(savedMileStone);
    }

    @Override
    public MileStoneDTO update(Long mileStoneId, MileStoneDTO mileStoneDTO) {

        MileStoneModel mileStone = mileStoneModelRepository.findById(mileStoneId).orElseThrow(
                ()->new ResourceNotFoundException("Milestone with id: " + mileStoneId + " not found")
        );

        MileStoneModel updatedMileStone=mileStoneMapper.partialUpdate(mileStoneDTO,mileStone);
        MileStoneModel savedMileStone = mileStoneModelRepository.save(updatedMileStone);

        return mileStoneMapper.toDto(savedMileStone);
    }

    @Override
    public void deleteMileStone(Long mileStoneId) {
        if(!mileStoneModelRepository.existsById(mileStoneId))
            throw new ResourceNotFoundException("Mile Stone with id: " + mileStoneId + " not found");
        mileStoneModelRepository.deleteById(mileStoneId);
    }

    @Override
    public List<MileStoneDTO> getAllMileStoneByContract(Long contractId) {

        ContractModel contract = contractsRepository.findById(contractId).orElseThrow(
                ()->new ResourceNotFoundException("Contract with Id: " + contractId + " not found")
        );

        List<MileStoneModel> milestones = mileStoneModelRepository.findAllByContract(contract);
        return mileStoneMapper.toDTOs(milestones);
    }


    @Override
    public MileStoneDTO setMilestoneAccepted(Long mileStoneId) {
        MileStoneModel mileStone = mileStoneModelRepository.findById(mileStoneId)
                .orElseThrow(() -> new ResourceNotFoundException("Milestone with id: " + mileStoneId + " not found"));

        if (mileStone.getStatus() != MileStoneStatus.PENDING) {
            throw new IllegalStateException("Milestone must be in PENDING state to be set to ACCEPTED");
        }

        mileStone.setStatus(MileStoneStatus.ACCEPTED);
        MileStoneModel savedMileStone = mileStoneModelRepository.save(mileStone);
        return mileStoneMapper.toDto(savedMileStone);
    }

    @Override
    public MileStoneDTO setMilestoneApproved(Long mileStoneId) {
        MileStoneModel mileStone = mileStoneModelRepository.findById(mileStoneId)
                .orElseThrow(() -> new ResourceNotFoundException("Milestone with id: " + mileStoneId + " not found"));

        if (mileStone.getStatus() != MileStoneStatus.ACCEPTED) {
            throw new IllegalStateException("Milestone must be in ACCEPTED state to be set to APPROVED");
        }

        mileStone.setStatus(MileStoneStatus.APPROVED);
        MileStoneModel savedMileStone = mileStoneModelRepository.save(mileStone);
        return mileStoneMapper.toDto(savedMileStone);
    }

    @Override
    public MileStoneDTO setMilestoneCompleted(Long mileStoneId) {
        MileStoneModel mileStone = mileStoneModelRepository.findById(mileStoneId)
                .orElseThrow(() -> new ResourceNotFoundException("Milestone with id: " + mileStoneId + " not found"));

        if (mileStone.getStatus() != MileStoneStatus.APPROVED) {
            throw new IllegalStateException("Milestone must be in APPROVED state to be set to COMPLETED");
        }

        mileStone.setStatus(MileStoneStatus.COMPLETED);
        MileStoneModel savedMileStone = mileStoneModelRepository.save(mileStone);
        return mileStoneMapper.toDto(savedMileStone);
    }

    @Override
    public MileStoneDTO setMilestoneReleased(Long mileStoneId) {
        MileStoneModel mileStone = mileStoneModelRepository.findById(mileStoneId)
                .orElseThrow(() -> new ResourceNotFoundException("Milestone with id: " + mileStoneId + " not found"));

        if (mileStone.getStatus() != MileStoneStatus.COMPLETED) {
            throw new IllegalStateException("Milestone must be in COMPLETED state to be set to RELEASED");
        }

        mileStone.setStatus(MileStoneStatus.RELEASED);
        MileStoneModel savedMileStone = mileStoneModelRepository.save(mileStone);
        return mileStoneMapper.toDto(savedMileStone);
    }

    @Override
    public MileStoneDTO setMilestoneDisputed(Long mileStoneId) {
        MileStoneModel mileStone = mileStoneModelRepository.findById(mileStoneId)
                .orElseThrow(() -> new ResourceNotFoundException("Milestone with id: " + mileStoneId + " not found"));

        // DISPUTED có thể được thiết lập từ bất kỳ trạng thái nào
        mileStone.setStatus(MileStoneStatus.DISPUTED);
        MileStoneModel savedMileStone = mileStoneModelRepository.save(mileStone);
        return mileStoneMapper.toDto(savedMileStone);
    }

    @Override
    public List<MileStoneDTO> acceptMilestones(Long proposalId, List<Long> milestoneIds) {
        ProposalModel proposal = proposalsRepository.findById(proposalId)
                .orElseThrow(() -> new ResourceNotFoundException("Proposal with Id: " + proposalId + " not found"));

        List<MileStoneModel> milestones = proposal.getMileStones().stream()
                .filter(m -> milestoneIds.contains(m.getMileStoneId()) && m.getStatus() == MileStoneStatus.PENDING)
                .collect(Collectors.toList());

        if (milestones.isEmpty()) {
            throw new IllegalStateException("No valid milestones found to accept");
        }

        milestones.forEach(m -> m.setStatus(MileStoneStatus.ACCEPTED));
        mileStoneModelRepository.saveAll(milestones);
        return mileStoneMapper.toDTOs(milestones);
    }

    @Override
    public void copyAcceptedMilestonesToContract(Long proposalId, Long contractId) {
        ProposalModel proposal = proposalsRepository.findById(proposalId)
                .orElseThrow(() -> new ResourceNotFoundException("Proposal with Id: " + proposalId + " not found"));

        ContractModel contract = contractsRepository.findById(contractId)
                .orElseThrow(() -> new ResourceNotFoundException("Contract with Id: " + contractId + " not found"));

        Set<MileStoneModel> acceptedMilestones = proposal.getMileStones().stream()
                .filter(m -> m.getStatus() == MileStoneStatus.ACCEPTED)
                .map(m -> {
                    MileStoneModel newMilestone = new MileStoneModel();
                    newMilestone.setDescription(m.getDescription());
                    newMilestone.setAmount(m.getAmount());
                    newMilestone.setDueDate(m.getDueDate());
                    newMilestone.setStatus(MileStoneStatus.APPROVED);
                    newMilestone.setContract(contract);
                    newMilestone.setProposal(null);
                    return newMilestone;
                })
                .collect(Collectors.toSet());

        if (acceptedMilestones.isEmpty()) {
            throw new IllegalStateException("No accepted milestones found to copy to contract");
        }

        mileStoneModelRepository.saveAll(acceptedMilestones);
        contract.getMileStones().addAll(acceptedMilestones);
        contractsRepository.save(contract);
    }


}
