package com.freelancemarketplace.backend.mapper;

import com.freelancemarketplace.backend.dto.ClarificationiProjectQANotificationDTO;
import com.freelancemarketplace.backend.model.ClarificationiProjectQANotificationModel;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

@Mapper(componentModel = "spring")
public interface ClarificationProjectNotificationMapper {

    ClarificationiProjectQANotificationDTO toDto(ClarificationiProjectQANotificationModel notificationModel);

}
