package com.freelancemarketplace.backend.mapper;

import com.freelancemarketplace.backend.dto.AdminDTO;
import com.freelancemarketplace.backend.model.AdminModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AdminMapper {
    AdminDTO toDTO(AdminModel admin);
    AdminModel toEntity(AdminDTO adminDTO);

    List<AdminDTO> toDTOs(List<AdminModel> admins);
}
