package com.freelancemarketplace.backend.notification.infrastructure.mapper;

import com.freelancemarketplace.backend.notification.dto.ClarificationiProjectQANotificationDTO;
import com.freelancemarketplace.backend.notification.domain.model.ClarificationProjectQANotificationModel;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ClarificationProjectNotificationMapper {

    ClarificationiProjectQANotificationDTO toDto(ClarificationProjectQANotificationModel notificationModel);

    List<ClarificationiProjectQANotificationDTO> toDtoList(List<ClarificationProjectQANotificationModel> notificationModels);

    List<ClarificationiProjectQANotificationDTO> toDTOs(List<ClarificationProjectQANotificationModel> notifications);
}
