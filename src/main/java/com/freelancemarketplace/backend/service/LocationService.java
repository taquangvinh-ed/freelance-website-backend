package com.freelancemarketplace.backend.service;

import com.freelancemarketplace.backend.dto.LocationDTO;

import java.util.List;

public interface LocationService {

    LocationDTO createLocation(LocationDTO locationDTO);

    LocationDTO updateLocation(Long locationId, LocationDTO locationDTO);

    void deleteLocation(Long locationId);

    List<LocationDTO> getAll();

}
