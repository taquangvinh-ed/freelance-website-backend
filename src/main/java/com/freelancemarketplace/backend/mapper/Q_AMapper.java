package com.freelancemarketplace.backend.mapper;

import com.freelancemarketplace.backend.dto.Q_ADTO;
import com.freelancemarketplace.backend.model.Q_AModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface Q_AMapper {

    @Mapping(target = "adminId", source = "admin.adminId")
    Q_ADTO toDTO(Q_AModel qA);

    Q_AModel toEntity(Q_ADTO qADTO);

    List<Q_ADTO> toDTOs(List<Q_AModel> qAS);

}
