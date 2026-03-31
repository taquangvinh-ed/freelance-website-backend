package com.freelancemarketplace.backend.infrastructure.mapper;

import com.freelancemarketplace.backend.dto.LocationDTO;
import com.freelancemarketplace.backend.domain.model.LocationModel;
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
