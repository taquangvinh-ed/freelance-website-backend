package com.freelancemarketplace.backend.mapper;

import com.freelancemarketplace.backend.dto.CategoryDTO;
import com.freelancemarketplace.backend.dto.SkillDTO;
import com.freelancemarketplace.backend.model.CategoryModel;
import com.freelancemarketplace.backend.model.SkillModel;
import org.mapstruct.*;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface CategoryMapper {

    CategoryModel toEntity(CategoryDTO categoryDTO);


    @Mapping(target = "skillIds", source = "skills", qualifiedByName = "mapSkillsToSkillIds")
    CategoryDTO toDto(CategoryModel categoryModel);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    CategoryModel partialUpdate(CategoryDTO categoryDTO, @MappingTarget CategoryModel categoryModel);

    SkillDTO toDTO(SkillModel skillModel);

    @Named("mapSkillsToSkillIds")
    default Set<Long>mapSkillsToSkillIds(Set<SkillModel> skills){
        if(skills == null)
            return null;
        return skills.stream().map(SkillModel::getSkillId).collect(Collectors.toSet());
    }

    List<CategoryDTO> toDTOs(List<CategoryModel> categoryModels);
}