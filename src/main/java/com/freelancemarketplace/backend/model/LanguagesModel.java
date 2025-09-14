package com.freelancemarketplace.backend.model;

import jakarta.persistence.*;

import java.util.Set;

@Entity
public class LanguagesModel extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long language_id;

    private String language_name;

    @ManyToMany(mappedBy = "client_languages")
    private Set<ClientsModel> clients;

    @ManyToMany(mappedBy = "freelancer_languages")
    private Set<FreelancersModel> freelancers;

    public LanguagesModel() {
    }

    public Long getLanguage_id() {
        return language_id;
    }

    public void setLanguage_id(Long language_id) {
        this.language_id = language_id;
    }

    public String getLanguage_name() {
        return language_name;
    }

    public void setLanguage_name(String language_name) {
        this.language_name = language_name;
    }

    public Set<ClientsModel> getClients() {
        return clients;
    }

    public void setClients(Set<ClientsModel> clients) {
        this.clients = clients;
    }

    public Set<FreelancersModel> getFreelancers() {
        return freelancers;
    }

    public void setFreelancers(Set<FreelancersModel> freelancers) {
        this.freelancers = freelancers;
    }
}
