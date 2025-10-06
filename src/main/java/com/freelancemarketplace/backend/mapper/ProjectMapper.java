package com.freelancemarketplace.backend.mapper;

import com.freelancemarketplace.backend.dto.BudgetDTO;
import com.freelancemarketplace.backend.dto.CategoryDTO;
import com.freelancemarketplace.backend.dto.ProjectDTO;
import com.freelancemarketplace.backend.dto.SkillDTO;
import com.freelancemarketplace.backend.model.BudgetModel;
import com.freelancemarketplace.backend.model.CategoryModel;
import com.freelancemarketplace.backend.model.ProjectModel;
import com.freelancemarketplace.backend.model.SkillModel;
import org.mapstruct.*;

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

    ProjectDTO toDto(ProjectModel projectModel);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    ProjectModel partialUpdate(ProjectDTO projectDTO, @MappingTarget ProjectModel projectModel);

    List<ProjectDTO> toDTOs(List<ProjectModel> projectModels);


}