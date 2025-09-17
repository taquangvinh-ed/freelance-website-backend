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
@Table(name = "Locations")
public class LocationModel extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long locationId;

    @ManyToOne
    @JoinColumn(name = "countryId")
    private CountryModel country;

    @ManyToOne
    @JoinColumn(name = "cityId")
    private CityModel city;

    private String details;

    @OneToMany(mappedBy = "location")
    private Set<AdminModel> admins;

    @OneToMany(mappedBy = "location")
    private Set<CompanyModel> companies;

    @OneToMany(mappedBy = "location")
    private Set<FreelancerModel> freelancers;

    @OneToMany(mappedBy = "location")
    private Set<ClientModel> clients;

    @OneToMany(mappedBy = "location")
    private Set<ExperienceModel> experiences;

}
