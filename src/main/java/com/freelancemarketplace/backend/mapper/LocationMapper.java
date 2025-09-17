package com.freelancemarketplace.backend.mapper;

import com.freelancemarketplace.backend.dto.LocationDTO;
import com.freelancemarketplace.backend.dto.NotificationDTO;
import com.freelancemarketplace.backend.model.LocationModel;
import com.freelancemarketplace.backend.model.NotificationModel;
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
