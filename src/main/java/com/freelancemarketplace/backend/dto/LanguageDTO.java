package com.freelancemarketplace.backend.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class LanguageDTO {

    private Long languageId;

    private String languageName;

    private String isoCode;

    private Boolean isActived;

}
