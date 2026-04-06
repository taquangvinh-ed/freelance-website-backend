package com.freelancemarketplace.backend.product.infrastructure.mapper;

import com.freelancemarketplace.backend.product.dto.ProductDTO;
import com.freelancemarketplace.backend.skill.dto.SkillDTO;
import com.freelancemarketplace.backend.product.domain.model.ProductModel;
import com.freelancemarketplace.backend.skill.domain.model.SkillModel;
import org.mapstruct.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface ProductMapper {

    ProductModel toEntity(ProductDTO productDTO);



    SkillDTO toDTO(SkillModel skillModel);

    @Mapping(target = "freelancerId", source = "freelancer.freelancerId")
    @Mapping(target = "skillIds", source = "skills", qualifiedByName = "mapSkillsToSkillIds")
    ProductDTO toDto(ProductModel productModel);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    ProductModel partialUpdate(ProductDTO productDTO, @MappingTarget ProductModel productModel);

    List<ProductDTO> toDTOs(List<ProductModel> productModels);

    default Page<ProductDTO> toDTOpage(Page<ProductModel> modelPage, Pageable pageable){
        List<ProductDTO> dtos = toDTOs(modelPage.getContent());
        return new PageImpl<>(dtos, pageable, modelPage.getTotalElements());
    }


    @Named("mapSkillsToSkillIds")
    default Set<Long>mapSkillsToSkillIds(Set<SkillModel> skillModels){
        if(skillModels == null)
            return null;
        return skillModels.stream().map(SkillModel::getSkillId).collect(Collectors.toSet());

    }

}