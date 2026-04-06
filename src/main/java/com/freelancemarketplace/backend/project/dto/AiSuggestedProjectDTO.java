package com.freelancemarketplace.backend.project.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import com.freelancemarketplace.backend.freelancer.domain.model.AdvancedPreferences;

@Getter
@Setter
@NoArgsConstructor
public class AiSuggestedProjectDTO {

    private String title;
    private String description;
    private String categoryName;
    private List<String> skillNames = new ArrayList<>();
    private Scope scope;
    private Budget budget;
    private AdvancedPreferences advancedPreferences;
    private Double confidence;
    private List<String> notes = new ArrayList<>();

    @Getter
    @Setter
    @NoArgsConstructor
    public static class Scope {
        private String duration;
        private String level;
        private String workload;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    public static class Budget {
        private String type;
        private String currencyUnit;
        private BigDecimal minValue;
        private BigDecimal maxValue;
        private BigDecimal fixedValue;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    public static class AdvancedPreferences {
        private String hoursPerWeek;
        private Boolean isRemote;
        private String preferredTimezone;
        private Integer minYearsExperience;
        private List<String> preferredLanguages = new ArrayList<>();
        private Boolean preferVerified;
        private Boolean preferTopRated;
    }
}

