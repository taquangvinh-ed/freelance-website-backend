package com.freelancemarketplace.backend.mapper;

import com.freelancemarketplace.backend.dto.CategoryDTO;
import com.freelancemarketplace.backend.dto.SkillDTO;
import com.freelancemarketplace.backend.dto.SkillDTO2;
import com.freelancemarketplace.backend.model.CategoryModel;
import com.freelancemarketplace.backend.model.SkillModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface SkillMapper {

    CategoryDTO toCategoryDTO(CategoryModel categoryModel);

    @Mapping(target = "categoryNames", source = "categories", qualifiedByName = "mapCategoriesToCategoryName")
    SkillDTO toDTO(SkillModel skillModel);

    SkillModel toEntity(SkillDTO skillDTO);

    List<SkillDTO> toDTOs(List<SkillModel> skills);

    SkillDTO2 toDTO2(SkillModel skillModel);

    SkillModel toEntity2(SkillDTO2 skillDTO);

    @Named("mapCategoriesToCategoryName")
    default Set<String>mapCategoriesToCategoryName(Set<CategoryModel> categories){
        if(categories != null)
            return categories.stream().map(CategoryModel::getName).collect(Collectors.toSet());
        return new HashSet<>();
    }

}
