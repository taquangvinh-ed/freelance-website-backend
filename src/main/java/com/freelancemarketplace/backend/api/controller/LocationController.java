package com.freelancemarketplace.backend.api.controller;

import com.freelancemarketplace.backend.location.dto.LocationDTO;
import com.freelancemarketplace.backend.api.response.ResponseDTO;
import com.freelancemarketplace.backend.api.response.ResponseMessage;
import com.freelancemarketplace.backend.api.response.ResponseStatusCode;
import com.freelancemarketplace.backend.location.application.service.LocationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<ResponseDTO>createLocation(@RequestBody LocationDTO locationDTO){
        LocationDTO createdLocation = locationService.createLocation(locationDTO);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new ResponseDTO(
                        ResponseStatusCode.CREATED,
                        ResponseMessage.CREATED,
                        createdLocation
                ));
    }

    @PutMapping("/{locationId}")
    ResponseEntity<ResponseDTO>updateLocation(@PathVariable Long locationId,
            @RequestBody LocationDTO locationDTO){
        LocationDTO updatedLocationDTO = locationService.updateLocation(locationId, locationDTO);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ResponseDTO(
                        ResponseStatusCode.SUCCESS,
                        ResponseMessage.SUCCESS,
                        updatedLocationDTO
                ));
    }

    @GetMapping("/getAll")
    ResponseEntity<ResponseDTO>getAll(){
       List<LocationDTO> locationDTOList =  locationService.getAll();
       return ResponseEntity
               .status(HttpStatus.OK)
               .body(new ResponseDTO(
                       ResponseStatusCode.SUCCESS,
                       ResponseMessage.SUCCESS,
                       locationDTOList));
    }

    @DeleteMapping("/{locationId}")
    ResponseEntity<ResponseDTO>deleteLocation(@PathVariable Long locationId){
        locationService.deleteLocation(locationId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ResponseDTO(
                        ResponseStatusCode.NO_CONTENT,
                        ResponseMessage.NO_CONTENT
                ));
    }
}
