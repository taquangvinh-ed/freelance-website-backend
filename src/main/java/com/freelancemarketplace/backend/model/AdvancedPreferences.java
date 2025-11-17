package com.freelancemarketplace.backend.model;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Embeddable
@Getter
@Setter
public class AdvancedPreferences {

    private String hoursPerWeek; // "less10", "10to20", "20to30", "30plus"

    // 100% Remote?
    private Boolean isRemote = true;

    // Múi giờ ưu tiên (GMT)
    private String preferredTimezone; // "GMT+7", "GMT-5", ...

    // Kinh nghiệm tối thiểu (năm)
    private Integer minYearsExperience;

    // Ngôn ngữ ưu tiên
    @ElementCollection
    private List<String> preferredLanguages = new ArrayList<>(); // ["English-Fluent", "Vietnamese-Native"]

    // Ưu tiên freelancer đã verify / top-rated
    private Boolean preferVerified;
    private Boolean preferTopRated;

}
