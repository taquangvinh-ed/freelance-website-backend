package com.freelancemarketplace.backend.project.api.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AiSuggestProjectRequest {

    @NotBlank(message = "prompt is required")
    private String prompt;

    private String locale;

    private String currency;

    @Valid
    private ClientContext clientContext;

    @Getter
    @Setter
    @NoArgsConstructor
    public static class ClientContext {
        private String companyName;
        private String industry;
        private String projectType;
        private String preferredTimezone;
        private String budgetPreference;
    }
}

