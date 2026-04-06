package com.freelancemarketplace.backend.skill.infrastructure.mapper;

import com.freelancemarketplace.backend.category.dto.CategoryDTO;
import com.freelancemarketplace.backend.category.dto.CategoryResponse;
import com.freelancemarketplace.backend.skill.dto.SkillDTO;
import com.freelancemarketplace.backend.skill.dto.SkillDTO2;
import com.freelancemarketplace.backend.category.domain.model.CategoryModel;
import com.freelancemarketplace.backend.skill.domain.model.SkillModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface SkillMapper {

    CategoryDTO toCategoryDTO(CategoryModel categoryModel);

    @Mapping(target = "categories", source = "categories", qualifiedByName = "mapCategoriesToCategoryResponses")
    SkillDTO toDTO(SkillModel skillModel);

    SkillModel toEntity(SkillDTO skillDTO);

    List<SkillDTO> toDTOs(List<SkillModel> skills);

    SkillDTO2 toDTO2(SkillModel skillModel);

    SkillModel toEntity2(SkillDTO2 skillDTO);

    @Named("mapCategoriesToCategoryResponses")
    default Set<CategoryResponse>mapCategoriesToCategoryName(Set<CategoryModel> categories){
        if(categories == null)
            return null;
            Set<CategoryResponse> categorySet = categories.stream().map(categoryModel-> {
                CategoryResponse category = new CategoryResponse();
                category.setCategoryId(categoryModel.getCategoryId());
                category.setName(categoryModel.getName());
                return category;
            }).collect(Collectors.toSet());
        return categorySet;
    }

}
