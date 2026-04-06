package com.freelancemarketplace.backend.product.infrastructure.repository;

import com.freelancemarketplace.backend.product.domain.model.OrderModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<OrderModel, Long> {
}
