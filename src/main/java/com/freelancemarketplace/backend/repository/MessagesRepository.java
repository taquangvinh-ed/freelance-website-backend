package com.freelancemarketplace.backend.repository;

import com.freelancemarketplace.backend.model.MessagesModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MessagesRepository extends JpaRepository<MessagesModel, Long> {
  }