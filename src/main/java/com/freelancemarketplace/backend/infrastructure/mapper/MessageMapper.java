package com.freelancemarketplace.backend.infrastructure.mapper;

import com.freelancemarketplace.backend.dto.MessageDTO;
import com.freelancemarketplace.backend.domain.model.MessageModel;
import org.mapstruct.*;

import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface MessageMapper {
    @Mapping(target = "createdAt", ignore = true)
    MessageModel toEntity(MessageDTO messageDTO);

    MessageDTO toDto(MessageModel messageModel);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    MessageModel partialUpdate(MessageDTO messageDTO, @MappingTarget MessageModel messageModel);

    List<MessageDTO>toDTOs(List<MessageModel> modelList);

}