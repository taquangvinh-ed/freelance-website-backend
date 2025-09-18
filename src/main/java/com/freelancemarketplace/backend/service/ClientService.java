package com.freelancemarketplace.backend.service;

import com.freelancemarketplace.backend.dto.ClientDTO;

public interface ClientService {

    ClientDTO createClient(ClientDTO clientDTO);

    ClientDTO updateClient(Long clientId, ClientDTO clientDTO);

    void deleteClient(Long clientId);

}
