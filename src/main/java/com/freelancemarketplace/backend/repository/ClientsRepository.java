package com.freelancemarketplace.backend.repository;

import com.freelancemarketplace.backend.model.ClientsModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientsRepository extends JpaRepository<ClientsModel, Long> {
  }