package com.akido.orderservice.services;

import com.akido.orderservice.dto.OrderDTO;
import com.akido.orderservice.entities.Order;
import com.akido.orderservice.repositories.OrderRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class OrderService {
    private final OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public List<OrderDTO> getAllOrders(){
        List<Order> all = orderRepository.findAll();
        List<OrderDTO> orderDTOs = new ArrayList<>();

        for (Order order : all) {
            orderDTOs.add(new OrderDTO(
                    order.getId(),
                    order.getUser().getId(),
                    order.getDescription(),
                    order.getStatus(),
                    order.getCreatedAt()));
        }
        return orderDTOs;
    }
}
