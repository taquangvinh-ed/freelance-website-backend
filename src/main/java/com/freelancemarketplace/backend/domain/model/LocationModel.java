package com.freelancemarketplace.backend.domain.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;
import com.freelancemarketplace.backend.admin.domain.model.AdminModel;
import com.freelancemarketplace.backend.audit.domain.model.BaseEntity;
import com.freelancemarketplace.backend.client.domain.model.ClientModel;
import com.freelancemarketplace.backend.client.domain.model.CompanyModel;
import com.freelancemarketplace.backend.freelancer.domain.model.ExperienceModel;
import com.freelancemarketplace.backend.freelancer.domain.model.FreelancerModel;

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
