package com.freelancemarketplace.backend.mapper;

import com.freelancemarketplace.backend.dto.TestimonialDTO;
import com.freelancemarketplace.backend.model.TestimonialModel;
import org.mapstruct.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface TestimonialMapper {

    TestimonialModel toEntity(TestimonialDTO testimonialDTO);

    @Mapping(target = "freelancerId", source = "freelancer.freelancerId")
    @Mapping(target = "teamId", source = "team.teamId")
    @Mapping(target = "clientId", source = "client.clientId")
    @Mapping(target = "projectId", source = "project.projectId")
    TestimonialDTO toDto(TestimonialModel testimonialModel);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    TestimonialModel partialUpdate(TestimonialDTO testimonialDTO, @MappingTarget TestimonialModel testimonialModel);

    default  Page<TestimonialDTO> toDTOPage(Page<TestimonialModel> modelpage, Pageable pageable){
        List<TestimonialDTO> dtoList = modelpage.getContent().stream()
                .map(this::toDto) // Use the single-item mapping method
                .collect(Collectors.toList());

        // 2. Create a concrete PageImpl with the new content and original pagination info
        return new PageImpl<>(
                dtoList,                   // The new list of DTOs
                pageable,                  // The original Pageable object
                modelpage.getTotalElements() // The total count from the original page
        );
    }
}