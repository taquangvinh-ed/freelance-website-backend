package com.freelancemarketplace.backend.service;

import com.freelancemarketplace.backend.dto.MileStoneDTO;
import com.freelancemarketplace.backend.model.ContractModel;

import java.util.List;

public interface MileStoneService {

    MileStoneDTO createMileStone(MileStoneDTO mileStoneDTO);

    MileStoneDTO update(Long mileStoneId, MileStoneDTO mileStoneDTO);

    void deleteMileStone(Long mileStoneId);

    List<MileStoneDTO> getAllMileStoneByContract(Long contractId);

    MileStoneDTO setMilestoneAccepted(Long mileStoneId);

    MileStoneDTO setMilestoneApproved(Long mileStoneId);

    MileStoneDTO setMilestoneCompleted(Long mileStoneId);

    MileStoneDTO setMilestoneReleased(Long mileStoneId);

    MileStoneDTO setMilestoneDisputed(Long mileStoneId);

    List<MileStoneDTO> acceptMilestones(Long proposalId, List<Long> milestoneIds);

    void copyAcceptedMilestonesToContract(Long proposalId, Long contractId);
}
