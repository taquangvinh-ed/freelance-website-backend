package com.freelancemarketplace.backend.repository;

import com.freelancemarketplace.backend.model.VideoModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VideosRepository extends JpaRepository<VideoModel, Long> {
  }