package com.freelancemarketplace.backend.mapper;

import com.freelancemarketplace.backend.dto.ClarificationiProjectQANotificationDTO;
import com.freelancemarketplace.backend.model.ClarificationProjectQANotificationModel;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ClarificationProjectNotificationMapper {

    ClarificationiProjectQANotificationDTO toDto(ClarificationProjectQANotificationModel notificationModel);

    List<ClarificationiProjectQANotificationDTO> toDtoList(List<ClarificationProjectQANotificationModel> notificationModels);

    List<ClarificationiProjectQANotificationDTO> toDTOs(List<ClarificationProjectQANotificationModel> notifications);
}
