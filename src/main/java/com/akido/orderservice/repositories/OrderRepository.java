package com.akido.orderservice.repositories;

import com.akido.orderservice.entities.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

public interface OrderRepository extends JpaRepository<Order, UUID> {
}
