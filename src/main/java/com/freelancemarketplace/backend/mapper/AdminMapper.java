package com.freelancemarketplace.backend.mapper;

import com.freelancemarketplace.backend.dto.AdminDTO;
import com.freelancemarketplace.backend.model.AdminModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface AdminMapper {
    AdminDTO toDTO(AdminModel admin);
    AdminModel toEntity(AdminDTO adminDTO);

    @Mapping(target = "adminId", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateFromDto(@MappingTarget AdminModel adminModel, AdminDTO adminDTO);
}
