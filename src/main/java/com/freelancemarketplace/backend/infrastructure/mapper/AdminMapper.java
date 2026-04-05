package com.freelancemarketplace.backend.infrastructure.mapper;

import com.freelancemarketplace.backend.admin.dto.AdminDTO;
import com.freelancemarketplace.backend.domain.model.AdminModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AdminMapper {
    AdminDTO toDTO(AdminModel admin);

    @Mapping(target = "adminId", ignore = true)
    AdminModel toEntity(AdminDTO adminDTO);

    List<AdminDTO> toDTOs(List<AdminModel> admins);
}
