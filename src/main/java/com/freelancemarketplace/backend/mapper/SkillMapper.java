package com.freelancemarketplace.backend.mapper;

import com.freelancemarketplace.backend.dto.SkillDTO;
import com.freelancemarketplace.backend.model.SkillModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface SkillMapper {

    SkillDTO toDTO(SkillModel skillModel);

    SkillModel toEntity(SkillDTO skillDTO);

    List<SkillDTO> toDTOs(List<SkillModel> skills);
}
