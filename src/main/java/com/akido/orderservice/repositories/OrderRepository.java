package com.akido.orderservice.repositories;

import com.akido.orderservice.entities.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface OrderRepository extends JpaRepository<Order, UUID> {
    Page<Order> findAllByUserId(UUID userId, Pageable pageable);
}

