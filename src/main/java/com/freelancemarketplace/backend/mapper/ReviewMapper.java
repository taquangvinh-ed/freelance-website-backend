package com.freelancemarketplace.backend.mapper;

import com.freelancemarketplace.backend.dto.ReviewDTO;
import com.freelancemarketplace.backend.dto.TestimonialDTO;
import com.freelancemarketplace.backend.model.TestimonialModel;
import org.mapstruct.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface ReviewMapper {

    @Mapping(target = "reviewerRole", ignore = true)
    @Mapping(target = "freelancer", ignore = true)
    @Mapping(target = "client", ignore = true)
    @Mapping(target = "project", ignore = true)
    @Mapping(target = "type", ignore = true)
    @Mapping(target = "datePosted", ignore = true)
    TestimonialModel toEntity(ReviewDTO reviewDTO);

    @Mapping(target = "freelancerId", source = "freelancer.freelancerId")
    @Mapping(target = "clientId", source = "client.clientId")
    @Mapping(target = "teamId", source = "team.teamId")
    ReviewDTO toDto(TestimonialModel testimonialModel);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    TestimonialModel partialUpdate(ReviewDTO reviewDTO, @MappingTarget TestimonialModel testimonialModel);


    default Page<ReviewDTO> toDTOPage(Page<TestimonialModel> modelpage, Pageable pageable){
        List<ReviewDTO> dtoList = modelpage.getContent().stream()
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