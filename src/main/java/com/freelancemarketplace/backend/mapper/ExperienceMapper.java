package com.freelancemarketplace.backend.mapper;

import com.freelancemarketplace.backend.dto.ExperienceDTO;
import com.freelancemarketplace.backend.model.ExperienceModel;
import org.mapstruct.*;

import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface ExperienceMapper {
    ExperienceModel toEntity(ExperienceDTO experienceDTO);

    @Mapping(target = "freelancerId", source = "freelancer.freelancerId")
    ExperienceDTO toDto(ExperienceModel experienceModel);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    ExperienceModel partialUpdate(ExperienceDTO experienceDTO, @MappingTarget ExperienceModel experienceModel);

    List<ExperienceDTO> toDtos(List<ExperienceModel> models);
}