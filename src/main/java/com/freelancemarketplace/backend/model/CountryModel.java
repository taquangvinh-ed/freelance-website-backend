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
@Table(name = "Countries")
public class CountryModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long countryId;

    private String countryName;

    @OneToMany(mappedBy = "country")
    private Set<LocationModel> locationModel;

}
