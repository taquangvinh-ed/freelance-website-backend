package com.freelancemarketplace.backend.mapper;

import com.freelancemarketplace.backend.dto.ProposalDTO;
import com.freelancemarketplace.backend.model.ProposalModel;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface ProposalMapper {
    ProposalModel toEntity(ProposalDTO proposalDTO);

    @Mapping(target = "freelancerId", source = "freelancer.freelancerId")
    @Mapping(target = "projectId", source = "project.projectId")
    @Mapping(target = "teamId", source = "team.teamId")
    ProposalDTO toDto(ProposalModel proposalModel);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    ProposalModel partialUpdate(ProposalDTO proposalDTO, @MappingTarget ProposalModel proposalModel);
}