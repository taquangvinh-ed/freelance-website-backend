package com.freelancemarketplace.backend.video.infrastructure.repository;

import com.freelancemarketplace.backend.freelancer.domain.model.VideoModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VideosRepository extends JpaRepository<VideoModel, Long> {
  }