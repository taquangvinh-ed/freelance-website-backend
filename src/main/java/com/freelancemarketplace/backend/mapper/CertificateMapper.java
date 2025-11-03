package com.freelancemarketplace.backend.mapper;

import com.freelancemarketplace.backend.dto.CertificateDTO;
import com.freelancemarketplace.backend.model.CertificateModel;
import org.mapstruct.*;

import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface CertificateMapper {
    CertificateModel toEntity(CertificateDTO certificateDTO);

    @Mapping(target = "freelancerId", source = "freelancer.freelancerId")
    CertificateDTO toDto(CertificateModel certificateModel);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    CertificateModel partialUpdate(CertificateDTO certificateDTO, @MappingTarget CertificateModel certificateModel);

    List<CertificateDTO> toDTOs(List<CertificateModel> certificateModels);
}