package com.freelancemarketplace.backend.service.imp;

import com.freelancemarketplace.backend.dto.MileStoneDTO;
import com.freelancemarketplace.backend.exception.ResourceNotFoundException;
import com.freelancemarketplace.backend.mapper.MileStoneMapper;
import com.freelancemarketplace.backend.model.ContractModel;
import com.freelancemarketplace.backend.model.MileStoneModel;
import com.freelancemarketplace.backend.repository.ContractsRepository;
import com.freelancemarketplace.backend.repository.MileStoneModelRepository;
import com.freelancemarketplace.backend.service.MileStoneService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MileStoneServiceImp implements MileStoneService {

    private MileStoneModelRepository mileStoneModelRepository;
    private MileStoneMapper mileStoneMapper;
    private ContractsRepository contractsRepository;

    public MileStoneServiceImp(MileStoneModelRepository mileStoneModelRepository, MileStoneMapper mileStoneMapper, ContractsRepository contractsRepository) {
        this.mileStoneModelRepository = mileStoneModelRepository;
        this.mileStoneMapper = mileStoneMapper;
        this.contractsRepository = contractsRepository;
    }

    @Override
    public MileStoneDTO createMileStone(MileStoneDTO mileStoneDTO) {

        MileStoneModel newMileStone = mileStoneMapper.toEntity(mileStoneDTO);

        ContractModel contract = contractsRepository.findById(mileStoneDTO.getContractId()).orElseThrow(
                ()->new ResourceNotFoundException("Contract with Id: " + mileStoneDTO.getContractId() + " not found")
        );

        newMileStone.setContract(contract);

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
}
