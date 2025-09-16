package com.freelancemarketplace.backend.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "Languages")
public class LanguageModel extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long languageId;

    @NotBlank(message = "language name must be required")
    private String languageName;

    @NotBlank(message = "iso code must be required")
    private String isoCode;

    private Boolean isActived;

    @ManyToMany(mappedBy = "languages")
    private Set<ClientModel> clients;

    @ManyToMany(mappedBy = "languages")
    private Set<FreelancerModel> freelancers;

}
