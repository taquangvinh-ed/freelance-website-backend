package com.freelancemarketplace.backend.mapper;

import com.freelancemarketplace.backend.dto.ProjectQuestionDTO;
import com.freelancemarketplace.backend.model.ProjectQuestionModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = org.mapstruct.ReportingPolicy.IGNORE)
public interface ProjectQuestionMapper {

    @Mapping(target = "projectId", source = "project.projectId")
    @Mapping(target = "askedByUserId", source = "askedBy.userId")
    @Mapping(target = "askedByUsername", source = "askedBy.username")
    @Mapping(target = "answeredByUserId", source = "answeredBy.userId")
    @Mapping(target = "answeredByUsername", source = "answeredBy.username")
    ProjectQuestionDTO toDto(ProjectQuestionModel model);

    List<ProjectQuestionDTO> toDtos(List<ProjectQuestionModel> models);
}

