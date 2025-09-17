package com.freelancemarketplace.backend.repository;

import com.freelancemarketplace.backend.model.AdminModel;
import com.freelancemarketplace.backend.model.Q_AModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface Q_ARepository extends JpaRepository<Q_AModel, Long> {
  List<Q_AModel> findAllByTag(String tag);
  List<Q_AModel> findAllByAdmin(AdminModel adminModel);
}