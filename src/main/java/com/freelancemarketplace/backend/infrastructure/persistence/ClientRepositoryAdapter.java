package com.freelancemarketplace.backend.infrastructure.persistence;

import com.freelancemarketplace.backend.application.port.ClientCrudPort;
import com.freelancemarketplace.backend.client.domain.model.ClientModel;
import com.freelancemarketplace.backend.client.infrastructure.repository.ClientsRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository Adapter: Client CRUD Repository (DIP - Dependency Inversion Principle)
 */
@Repository
@AllArgsConstructor
@Slf4j
public class ClientRepositoryAdapter implements ClientCrudPort {
    
    private final ClientsRepository clientsRepository;
    
    @Override
    public ClientModel save(ClientModel entity) {
        log.debug("Saving client: {}", entity.getClientId());
        return clientsRepository.save(entity);
    }
    
    @Override
    public Optional<ClientModel> findById(Long id) {
        log.debug("Finding client by ID: {}", id);
        return clientsRepository.findById(id);
    }
    
    @Override
    public List<ClientModel> findAll() {
        log.debug("Finding all clients");
        return clientsRepository.findAll();
    }
    
    @Override
    public void deleteById(Long id) {
        log.debug("Deleting client by ID: {}", id);
        clientsRepository.deleteById(id);
    }
    
    @Override
    public boolean existsById(Long id) {
        log.debug("Checking if client exists: {}", id);
        return clientsRepository.existsById(id);
    }
    
    @Override
    public long count() {
        log.debug("Counting all clients");
        return clientsRepository.count();
    }
    
    @Override
    public ClientModel update(ClientModel entity) {
        log.debug("Updating client: {}", entity.getClientId());
        return clientsRepository.save(entity);
    }
    
    @Override
    public Optional<ClientModel> findByUserId(Long userId) {
        log.debug("Finding client by user ID: {}", userId);
        return clientsRepository.findById(userId);
    }
}

