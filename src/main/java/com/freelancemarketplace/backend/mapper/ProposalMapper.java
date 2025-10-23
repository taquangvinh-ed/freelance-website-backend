package com.freelancemarketplace.backend.mapper;

import com.freelancemarketplace.backend.dto.MileStoneDTO;
import com.freelancemarketplace.backend.dto.ProposalDTO;
import com.freelancemarketplace.backend.model.MileStoneModel;
import com.freelancemarketplace.backend.model.ProposalModel;
import org.mapstruct.*;

import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface ProposalMapper {
    ProposalModel toEntity(ProposalDTO proposalDTO);

    @Mapping(target = "freelancerId", ignore = true)
    @Mapping(target = "contractId", ignore = true)
    @Mapping(target = "proposalId", ignore = true)
    MileStoneDTO toMileStoneDTO(MileStoneModel model);

    MileStoneModel toMileStoneEntity(MileStoneDTO mileStoneDTO);

    @Mapping(target = "projectId", source = "project.projectId")
    @Mapping(target = "teamId", source = "team.teamId")
    ProposalDTO toDto(ProposalModel proposalModel);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    ProposalModel partialUpdate(ProposalDTO proposalDTO, @MappingTarget ProposalModel proposalModel);

    List<ProposalDTO> toDTOs(List<ProposalModel> proposals);
}