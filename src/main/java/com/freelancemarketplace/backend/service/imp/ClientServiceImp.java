package com.freelancemarketplace.backend.service.imp;

import com.freelancemarketplace.backend.dto.ClientDTO;
import com.freelancemarketplace.backend.exception.ResourceNotFoundException;
import com.freelancemarketplace.backend.mapper.ClientMapper;
import com.freelancemarketplace.backend.model.ClientModel;
import com.freelancemarketplace.backend.repository.ClientsRepository;
import com.freelancemarketplace.backend.service.ClientService;
import org.springframework.stereotype.Service;

@Service
public class ClientServiceImp implements ClientService {

    ClientsRepository clientsRepository;
    ClientMapper clientMapper;

    public ClientServiceImp(ClientsRepository clientsRepository, ClientMapper clientMapper) {
        this.clientsRepository = clientsRepository;
        this.clientMapper = clientMapper;
    }

    @Override
    public ClientDTO createClient(ClientDTO clientDTO) {

        ClientModel newClient = clientMapper.toEntity(clientDTO);
        ClientModel savedClient = clientsRepository.save(newClient);

        return clientMapper.toDto(savedClient);
    }

    @Override
    public ClientDTO updateClient(Long clientId, ClientDTO clientDTO) {

        ClientModel client = clientsRepository.findById(clientId).orElseThrow(
                ()->new ResourceNotFoundException("Client with id: " + clientId+ " no found")
        );

        ClientModel updatedClient = clientMapper.partialUpdate(clientDTO, client);

        ClientModel savedClient = clientsRepository.save(updatedClient);

        return clientMapper.toDto(savedClient);
    }

    @Override
    public void deleteClient(Long clientId) {
        if(!clientsRepository.existsById(clientId))
            throw new ResourceNotFoundException("Client with id: " + clientId+ " no found");
    clientsRepository.deleteById(clientId);
    }
}
