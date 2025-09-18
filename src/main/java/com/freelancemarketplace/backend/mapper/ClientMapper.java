package com.freelancemarketplace.backend.mapper;

import com.freelancemarketplace.backend.dto.ClientDTO;
import com.freelancemarketplace.backend.model.ClientModel;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface ClientMapper {
    ClientModel toEntity(ClientDTO clientDTO);

    ClientDTO toDto(ClientModel clientModel);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    ClientModel partialUpdate(ClientDTO clientDTO, @MappingTarget ClientModel clientModel);
}