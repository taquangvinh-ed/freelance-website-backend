package com.freelancemarketplace.backend.location.infrastructure.mapper;

import com.freelancemarketplace.backend.location.dto.LocationDTO;
import com.freelancemarketplace.backend.notification.dto.NotificationDTO;
import com.freelancemarketplace.backend.location.domain.model.LocationModel;
import com.freelancemarketplace.backend.notification.domain.model.NotificationModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface LocationMapper {

    @Mapping(target = "countryName", source = "country.countryName")
    @Mapping(target = "cityName", source = "city.cityName")
    LocationDTO toDTO(LocationModel locationModel);

    LocationModel toEntity(LocationDTO locationDTO);

    List<LocationDTO> toDTOs(List<LocationModel> locations);
}
