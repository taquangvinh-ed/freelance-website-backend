package com.freelancemarketplace.backend.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
public class Bio implements Serializable {

    private LocalDate dateOfBirth;



}
