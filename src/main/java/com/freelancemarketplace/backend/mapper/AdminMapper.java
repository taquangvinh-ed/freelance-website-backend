package com.freelancemarketplace.backend.mapper;

import com.freelancemarketplace.backend.dto.AdminDTO;
import com.freelancemarketplace.backend.model.AdminsModel;
import org.mapstruct.Mapper;

@Mapper
public interface AdminMapper {
    AdminDTO toDTO(AdminsModel admin);
    AdminsModel toEntity(AdminDTO adminDTO);
}
