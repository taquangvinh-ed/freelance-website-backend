package com.freelancemarketplace.backend.infrastructure.mapper;

import com.freelancemarketplace.backend.certification.domain.model.CertificateModel;
import com.freelancemarketplace.backend.freelancer.domain.model.ExperienceModel;
import com.freelancemarketplace.backend.freelancer.domain.model.FreelancerModel;
import com.freelancemarketplace.backend.skill.domain.model.SkillModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;
import java.util.Set;
import com.freelancemarketplace.backend.certification.dto.CertificateDTO;
import com.freelancemarketplace.backend.freelancer.dto.ExperienceDTO;
import com.freelancemarketplace.backend.freelancer.dto.FreelancerDTO;
import com.freelancemarketplace.backend.freelancer.dto.FreelancerInfoDTO;
import com.freelancemarketplace.backend.skill.dto.SkillDTO;

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

    FreelancerModel partialUpdate(FreelancerDTO profile, @MappingTarget FreelancerModel freelancer);
//
}
