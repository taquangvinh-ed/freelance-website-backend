package com.freelancemarketplace.backend.mapper;

import com.freelancemarketplace.backend.dto.FreelancerDTO;
import com.freelancemarketplace.backend.dto.SkillDTO;
import com.freelancemarketplace.backend.model.FreelancerModel;
import com.freelancemarketplace.backend.model.SkillModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;
import java.util.Set;

@Mapper(componentModel = "spring")
public interface FreelancerMapper {

//    @Mapping(target = "skills", source = "skills", qualifiedByName = "mapSkills")
    FreelancerDTO toDTO(FreelancerModel freelancerModel);


    FreelancerModel toEntity(FreelancerDTO freelancerDTO);

    List<FreelancerDTO>toDTOs(List<FreelancerModel> freelancerModelList);

    SkillDTO toSkillDTO(SkillModel skill);


    Set<SkillDTO> toSkillDTOs(Set<SkillModel> skills);
//
}
