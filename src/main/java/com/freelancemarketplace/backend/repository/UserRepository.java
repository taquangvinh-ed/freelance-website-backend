package com.freelancemarketplace.backend.repository;

import com.freelancemarketplace.backend.model.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserModel, Long>, JpaSpecificationExecutor<UserModel> {
  Optional<UserModel> findByEmail(String email);
  Optional<UserModel> findByUsername(String username);
  Boolean existsByUsername(String username);
  Boolean existsByEmail(String email);

}