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
public class LanguagesModel extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long language_id;

    private String language_name;

    @ManyToMany(mappedBy = "client_languages")
    private Set<ClientsModel> clients;

    @ManyToMany(mappedBy = "freelancer_languages")
    private Set<FreelancersModel> freelancers;

}
