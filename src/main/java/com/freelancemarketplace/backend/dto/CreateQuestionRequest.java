package com.freelancemarketplace.backend.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CreateQuestionRequest {
    @NotBlank(message = "questionText must not be blank")
    private String questionText;
}

