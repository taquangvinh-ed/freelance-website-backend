package com.freelancemarketplace.backend.mapper;

import com.freelancemarketplace.backend.dto.FreelancerDTO;
import com.freelancemarketplace.backend.model.FreelancerModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface FreelancerMapper {
    FreelancerDTO toDTO(FreelancerModel freelancerModel);

    @Mapping(target = "freelancerId", ignore = true)
    FreelancerModel toEntity(FreelancerDTO freelancerDTO);

    List<FreelancerDTO>toDTOs(List<FreelancerModel> freelancerModelList);
}
