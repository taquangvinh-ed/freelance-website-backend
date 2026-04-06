package com.freelancemarketplace.backend.infrastructure.persistence;

import com.freelancemarketplace.backend.application.port.FreelancerCrudPort;
import com.freelancemarketplace.backend.freelancer.domain.model.FreelancerModel;
import com.freelancemarketplace.backend.repository.FreelancersRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository Adapter: Freelancer CRUD Repository (DIP - Dependency Inversion Principle)
 */
@Repository
@AllArgsConstructor
@Slf4j
public class FreelancerRepositoryAdapter implements FreelancerCrudPort {
    
    private final FreelancersRepository freelancerRepository;
    
    @Override
    public FreelancerModel save(FreelancerModel entity) {
        log.debug("Saving freelancer: {}", entity.getFreelancerId());
        return freelancerRepository.save(entity);
    }
    
    @Override
    public Optional<FreelancerModel> findById(Long id) {
        log.debug("Finding freelancer by ID: {}", id);
        return freelancerRepository.findById(id);
    }
    
    @Override
    public List<FreelancerModel> findAll() {
        log.debug("Finding all freelancers");
        return freelancerRepository.findAll();
    }
    
    @Override
    public void deleteById(Long id) {
        log.debug("Deleting freelancer by ID: {}", id);
        freelancerRepository.deleteById(id);
    }
    
    @Override
    public boolean existsById(Long id) {
        log.debug("Checking if freelancer exists: {}", id);
        return freelancerRepository.existsById(id);
    }
    
    @Override
    public long count() {
        log.debug("Counting all freelancers");
        return freelancerRepository.count();
    }
    
    @Override
    public FreelancerModel update(FreelancerModel entity) {
        log.debug("Updating freelancer: {}", entity.getFreelancerId());
        return freelancerRepository.save(entity);
    }
    
    @Override
    public Optional<FreelancerModel> findByUserId(Long userId) {
        log.debug("Finding freelancer by user ID: {}", userId);
        return freelancerRepository.findById(userId);
    }
}

