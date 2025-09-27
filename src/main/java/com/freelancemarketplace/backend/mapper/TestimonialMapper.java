package com.freelancemarketplace.backend.mapper;

import com.freelancemarketplace.backend.dto.TestimonialDTO;
import com.freelancemarketplace.backend.model.TestimonialModel;
import org.mapstruct.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface TestimonialMapper {

    TestimonialModel toEntity(TestimonialDTO testimonialDTO);

    @Mapping(target = "freelancerId", source = "freelancer.freelancerId")
    @Mapping(target = "teamId", source = "team.teamId")
    @Mapping(target = "clientId", source = "client.clientId")
    @Mapping(target = "companyId", source = "company.companyId")
    @Mapping(target = "projectId", source = "project.projectId")
    TestimonialDTO toDto(TestimonialModel testimonialModel);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    TestimonialModel partialUpdate(TestimonialDTO testimonialDTO, @MappingTarget TestimonialModel testimonialModel);

    Page<TestimonialDTO> toDTOPage(Page<TestimonialModel> modelpage, Pageable pageable);
}