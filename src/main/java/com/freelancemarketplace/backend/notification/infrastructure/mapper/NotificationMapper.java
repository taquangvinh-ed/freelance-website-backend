package com.freelancemarketplace.backend.notification.infrastructure.mapper;

import com.freelancemarketplace.backend.language.dto.LanguageDTO;
import com.freelancemarketplace.backend.notification.dto.NotificationDTO;
import com.freelancemarketplace.backend.language.domain.model.LanguageModel;
import com.freelancemarketplace.backend.notification.domain.model.NotificationModel;
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
