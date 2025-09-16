package com.freelancemarketplace.backend.mapper;

import com.freelancemarketplace.backend.dto.AdminDTO;
import com.freelancemarketplace.backend.model.AdminModel;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AdminMapper {
    AdminDTO toDTO(AdminModel admin);
    AdminModel toEntity(AdminDTO adminDTO);
}
