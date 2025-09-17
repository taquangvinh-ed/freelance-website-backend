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
@Table(name = "Cities")
public class CityModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cityId;

    private String cityName;

    @OneToMany(mappedBy = "city")
    private Set<LocationModel> locations;

}
