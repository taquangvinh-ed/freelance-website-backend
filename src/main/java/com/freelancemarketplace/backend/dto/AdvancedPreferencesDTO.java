package com.freelancemarketplace.backend.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class AdvancedPreferencesDTO {
    private String hoursPerWeek;
    private Boolean isRemote;
    private String preferredTimezone;
    private Integer minYearsExperience;
    private List<String> preferredLanguages;
    private Boolean preferVerified;
    private Boolean preferTopRated;
}
