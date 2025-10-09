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

    MileStoneDTO toMilStoneDTO(MileStoneModel model);
    MileStoneModel toMiStoneEntity(MileStoneDTO mileStoneDTO);

    @Mapping(target = "projectId", source = "project.projectId")
    @Mapping(target = "teamId", source = "team.teamId")
    ProposalDTO toDto(ProposalModel proposalModel);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    ProposalModel partialUpdate(ProposalDTO proposalDTO, @MappingTarget ProposalModel proposalModel);

    List<ProposalDTO> toDTOs(List<ProposalModel> proposals);
}