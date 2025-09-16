package com.freelancemarketplace.backend.mapper;

import com.freelancemarketplace.backend.dto.FreelancerDTO;
import com.freelancemarketplace.backend.model.FreelancerModel;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface FreelancerMapper {
    FreelancerDTO toDTO(FreelancerModel freelancerModel);
    FreelancerModel toEntity(FreelancerDTO freelancerDTO);
    List<FreelancerDTO>toDTOs(List<FreelancerModel> freelancerModelList);
}
