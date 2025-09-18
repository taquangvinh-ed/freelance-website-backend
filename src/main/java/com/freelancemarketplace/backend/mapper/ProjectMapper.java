package com.freelancemarketplace.backend.mapper;

import com.freelancemarketplace.backend.dto.ProjectDTO;
import com.freelancemarketplace.backend.model.ProjectModel;
import org.mapstruct.*;

import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface ProjectMapper {
    ProjectModel toEntity(ProjectDTO projectDTO);

    ProjectDTO toDto(ProjectModel projectModel);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    ProjectModel partialUpdate(ProjectDTO projectDTO, @MappingTarget ProjectModel projectModel);

    List<ProjectDTO> toDTOs(List<ProjectModel> projectModels);
}