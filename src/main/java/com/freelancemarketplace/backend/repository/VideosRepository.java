package com.freelancemarketplace.backend.repository;

import com.freelancemarketplace.backend.model.VideosModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VideosRepository extends JpaRepository<VideosModel, Long> {
  }