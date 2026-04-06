package com.freelancemarketplace.backend.project.infrastructure.mapper;

import com.freelancemarketplace.backend.project.api.request.CreateProjectRequest;
import org.mapstruct.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;
import com.freelancemarketplace.backend.category.domain.model.CategoryModel;
import com.freelancemarketplace.backend.category.dto.CategoryDTO;
import com.freelancemarketplace.backend.project.domain.model.BudgetModel;
import com.freelancemarketplace.backend.project.domain.model.ProjectModel;
import com.freelancemarketplace.backend.project.domain.model.ProjectScope;
import com.freelancemarketplace.backend.project.dto.BudgetDTO;
import com.freelancemarketplace.backend.project.dto.ProjectDTO;
import com.freelancemarketplace.backend.project.dto.ProjectScopeDTO;
import com.freelancemarketplace.backend.skill.domain.model.SkillModel;
import com.freelancemarketplace.backend.skill.dto.SkillDTO;

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
    ProjectScopeDTO toScopeDTO(ProjectScope projectScope);

    @Mapping(target = "clientId", source = "client.clientId")
    @Mapping(target = "scope", source = "scope", qualifiedByName = "mapScope")
    ProjectDTO toDto(ProjectModel projectModel);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    ProjectModel partialUpdate(ProjectDTO projectDTO, @MappingTarget ProjectModel projectModel);

    List<ProjectDTO> toDTOs(List<ProjectModel> projectModels);

    default Page<ProjectDTO> toDTOPage(List<ProjectDTO> dtos, Pageable pageable, long totalElements) {
        // Tạo PageImpl từ List<ProjectDTO> đã có, Pageable và tổng số phần tử
        return new PageImpl<>(dtos, pageable, totalElements);
    }

    ProjectModel toEntity(CreateProjectRequest request);

    @Named("mapScope")
    default ProjectScopeDTO mapScope(ProjectScope scope) {
        if (scope == null) return null;
        ProjectScopeDTO dto = new ProjectScopeDTO();
        dto.setDuration(scope.getDuration());
        dto.setLevel(scope.getLevel());
        dto.setWorkload(scope.getWorkload());
        return dto;
    }


}