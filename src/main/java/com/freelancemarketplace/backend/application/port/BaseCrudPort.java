package com.freelancemarketplace.backend.application.port;

import java.util.List;
import java.util.Optional;

/**
 * Port: Interface for basic CRUD operations (DIP - Dependency Inversion Principle)
 * Implementations should be in infrastructure/persistence layer
 * 
 * @param <T> Entity type
 * @param <ID> Primary key type
 */
public interface BaseCrudPort<T, ID> {
    
    /**
     * Create/Save entity
     * @param entity Entity to save
     * @return Saved entity
     */
    T save(T entity);
    
    /**
     * Find entity by ID
     * @param id Entity ID
     * @return Optional containing entity if found
     */
    Optional<T> findById(ID id);
    
    /**
     * Find all entities
     * @return List of all entities
     */
    List<T> findAll();
    
    /**
     * Delete entity by ID
     * @param id Entity ID
     */
    void deleteById(ID id);
    
    /**
     * Check if entity exists
     * @param id Entity ID
     * @return true if exists, false otherwise
     */
    boolean existsById(ID id);
    
    /**
     * Count total entities
     * @return Total count
     */
    long count();
    
    /**
     * Update entity
     * @param entity Entity to update
     * @return Updated entity
     */
    T update(T entity);
}

