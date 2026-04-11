package com.freelancemarketplace.backend.user.application.service;

import com.freelancemarketplace.backend.client.domain.model.ClientModel;
import com.freelancemarketplace.backend.user.domain.model.UserModel;
import com.freelancemarketplace.backend.user.dto.RegistrationtDTO;
import org.springframework.stereotype.Service;

@Service("clientFactory")
public class ClientFactory implements UserFactory<ClientModel> {

    @Override
    public ClientModel createProfile(RegistrationtDTO registrationDTO, UserModel savedUser) {
        ClientModel client = new ClientModel();
        client.setClientId(savedUser.getUserId());
        client.setFirstName(registrationDTO.getFirstName());
        client.setLastName(registrationDTO.getLastName());
        client.setUser(savedUser);

        if (registrationDTO.getSummary() != null) {
            client.getBio().setSummary(registrationDTO.getSummary());
        }
        if (registrationDTO.getFacebookUrl() != null) {
            client.getBio().setFacebookLink(registrationDTO.getFacebookUrl());
        }
        if (registrationDTO.getLinkedlnUrl() != null) {
            client.getBio().setLinkedinLink(registrationDTO.getLinkedlnUrl());
        }
        if (registrationDTO.getGithubUrl() != null) {
            client.getBio().setTwitterLink(registrationDTO.getGithubUrl());
        }

        return client;
    }
}
