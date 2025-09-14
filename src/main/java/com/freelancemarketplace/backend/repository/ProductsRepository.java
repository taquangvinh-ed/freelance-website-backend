package com.freelancemarketplace.backend.repository;

import com.freelancemarketplace.backend.model.ProductsModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductsRepository extends JpaRepository<ProductsModel, Long> {
  }