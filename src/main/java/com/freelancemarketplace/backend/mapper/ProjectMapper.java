package com.freelancemarketplace.backend.mapper;

import com.freelancemarketplace.backend.dto.*;
import com.freelancemarketplace.backend.model.*;
import com.freelancemarketplace.backend.request.CreateProjectRequest;
import org.mapstruct.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface ProjectMapper {
    ProjectModel toEntity(ProjectDTO projectDTO);

    @Mapping(target="projectId", source = "project.projectId")
    BudgetDTO toDto(BudgetModel budgetModel);
    BudgetModel toEntity(BudgetDTO budgetDTO);

    SkillModel toEntity(SkillDTO skillDTO);
    SkillDTO toDto(SkillModel skillModel);

    CategoryModel toEntity(CategoryDTO categoryDTO);
    CategoryDTO toCategoryDTO(CategoryModel categoryModel);


    ProjectScope toScopeEntity(ProjectScopeDTO projectScopeDTO);
    ProjectScopeDTO toScopeDTP(ProjectScope projectScope);

    @Mapping(target = "clientId", source = "client.clientId")
    ProjectDTO toDto(ProjectModel projectModel);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    ProjectModel partialUpdate(ProjectDTO projectDTO, @MappingTarget ProjectModel projectModel);

    List<ProjectDTO> toDTOs(List<ProjectModel> projectModels);

    default Page<ProjectDTO> toDTOPage(List<ProjectDTO> dtos, Pageable pageable, long totalElements) {
        // Tạo PageImpl từ List<ProjectDTO> đã có, Pageable và tổng số phần tử
        return new PageImpl<>(dtos, pageable, totalElements);
    }

    ProjectModel toEntity(CreateProjectRequest request);

}