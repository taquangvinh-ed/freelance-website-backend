package com.freelancemarketplace.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class TogglUserRequestDTO {
    private String name;
    private String email;
}
