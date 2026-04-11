package com.freelancemarketplace.backend.location.api.controller;

import com.freelancemarketplace.backend.location.dto.LocationDTO;
import com.freelancemarketplace.backend.common.api.response.ApiResponse;
import com.freelancemarketplace.backend.location.application.service.LocationService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/api/locations", produces = {MediaType.APPLICATION_JSON_VALUE})
public class LocationController {

    private LocationService locationService;

    public LocationController(LocationService locationService) {
        this.locationService = locationService;
    }

    @PostMapping("/")
    public ApiResponse<?> createLocation(@RequestBody LocationDTO locationDTO){
        LocationDTO createdLocation = locationService.createLocation(locationDTO);
        return ApiResponse.created(createdLocation);
    }

    @PutMapping("/{locationId}")
    ApiResponse<?> updateLocation(@PathVariable Long locationId,
            @RequestBody LocationDTO locationDTO){
        LocationDTO updatedLocationDTO = locationService.updateLocation(locationId, locationDTO);
        return ApiResponse.success(updatedLocationDTO);
    }

    @GetMapping("/getAll")
    ApiResponse<?> getAll(){
       List<LocationDTO> locationDTOList =  locationService.getAll();
       return ApiResponse.success(locationDTOList);
    }

    @DeleteMapping("/{locationId}")
    ApiResponse<?> deleteLocation(@PathVariable Long locationId){
        locationService.deleteLocation(locationId);
        return ApiResponse.noContent();
    }
}
