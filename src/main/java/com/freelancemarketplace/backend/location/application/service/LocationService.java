package com.freelancemarketplace.backend.location.application.service;

import com.freelancemarketplace.backend.location.dto.LocationDTO;

import java.util.List;

public interface LocationService {

    LocationDTO createLocation(LocationDTO locationDTO);

    LocationDTO updateLocation(Long locationId, LocationDTO locationDTO);

    void deleteLocation(Long locationId);

    List<LocationDTO> getAll();

}
