package com.freelancemarketplace.backend.mapper;

import com.freelancemarketplace.backend.dto.LanguageDTO;
import com.freelancemarketplace.backend.dto.NotificationDTO;
import com.freelancemarketplace.backend.model.LanguageModel;
import com.freelancemarketplace.backend.model.NotificationModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface NotificationMapper {

    @Mapping(source = "admin.adminId", target = "adminId")
    NotificationDTO toDTO(NotificationModel notificationModel);

    NotificationModel toEntity(NotificationDTO notificationDTO);

    List<NotificationDTO> toDTOs(List<NotificationModel> notifications);
}
