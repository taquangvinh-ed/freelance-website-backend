package com.freelancemarketplace.backend.model;

import jakarta.persistence.*;
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

    private String languageName;

    @ManyToMany(mappedBy = "languages")
    private Set<ClientModel> clients;

    @ManyToMany(mappedBy = "languages")
    private Set<FreelancerModel> freelancers;

}
