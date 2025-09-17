package com.freelancemarketplace.backend.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class LocationDTO {
    private Long locationId;
    private String countryName;
    private String cityName;
    private String details;
}
