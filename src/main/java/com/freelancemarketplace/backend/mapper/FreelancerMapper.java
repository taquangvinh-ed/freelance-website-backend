package com.freelancemarketplace.backend.mapper;

import com.freelancemarketplace.backend.dto.*;
import com.freelancemarketplace.backend.model.CertificateModel;
import com.freelancemarketplace.backend.model.ExperienceModel;
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

    @Mapping(target="description", source="bio.summary")
    FreelancerInfoDTO toInfoDTO(FreelancerModel freelancerModel);

    SkillDTO toSkillDTO(SkillModel skill);

    ExperienceDTO toExperienceDTO(ExperienceModel experienceModel);


    Set<SkillDTO> toSkillDTOs(Set<SkillModel> skills);
    CertificateDTO toCertificateDTO(CertificateModel certificateModel);
    Set<CertificateDTO> toCertificateDTOs(Set<CertificateModel> certificateModels);
    Set<ExperienceDTO> toExperienceDtos(Set<ExperienceModel> experienceModels);
//
}
