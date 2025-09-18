package com.freelancemarketplace.backend.mapper;

import com.freelancemarketplace.backend.dto.ContractDTO;
import com.freelancemarketplace.backend.dto.MileStoneDTO;
import com.freelancemarketplace.backend.model.ContractModel;
import com.freelancemarketplace.backend.model.MileStoneModel;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface ContractMapper {
    ContractModel toEntity(ContractDTO contractDTO);

    @AfterMapping
    default void linkMileStones(@MappingTarget ContractModel contractModel) {
        contractModel.getMileStones().forEach(mileStone -> mileStone.setContract(contractModel));
    }

    @Mapping(target = "contractId", source = "contract.contractId")
    MileStoneDTO toDto(MileStoneModel mileStoneModel);


    @Mapping(target = "proposalId", source = "proposal.proposalId")
    @Mapping(target = "freelancerId", source = "freelancer.freelancerId")
    @Mapping(target = "teamId", source = "team.teamId")
    @Mapping(target = "companyId", source = "company.companyId")
    @Mapping(target = "clientId", source = "client.clientId")
    @Mapping(target = "contractProjectId", source = "contractProject.projectId")
    ContractDTO toDto(ContractModel contractModel);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    ContractModel partialUpdate(ContractDTO contractDTO, @MappingTarget ContractModel contractModel);
}