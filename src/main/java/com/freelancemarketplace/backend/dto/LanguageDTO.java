package com.freelancemarketplace.backend.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LanguageDTO {

    @NotEmpty(message = "Language name cannot be empty")
    private String languageName;

}
