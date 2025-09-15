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
public class LocationsModel extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long location_id;

    private String postcode;
    private String country;
    private String state;
    private String city;
    private String details;


    @OneToMany(mappedBy = "location")
    private Set<AdminsModel> adminList;

    @OneToMany(mappedBy = "company_location")
    private Set<CompaniesModel> companyList;

    @OneToMany(mappedBy = "freelancer_location")
    private Set<FreelancersModel> freelancerList;

    @OneToMany(mappedBy = "client_location")
    private Set<ClientsModel> clientList;

    @OneToMany(mappedBy = "experience_location")
    private Set<ExperiencesModel> experienceList;

}
