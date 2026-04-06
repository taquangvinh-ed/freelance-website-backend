package com.freelancemarketplace.backend.client.application.service;

import com.freelancemarketplace.backend.client.dto.ClientDTO;

public interface ClientService {

    ClientDTO createClient(ClientDTO clientDTO);

    ClientDTO updateClient(Long clientId, ClientDTO clientDTO);

    void deleteClient(Long clientId);

}
