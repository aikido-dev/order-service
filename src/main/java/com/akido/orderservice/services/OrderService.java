package com.akido.orderservice.services;

import com.akido.orderservice.dto.OrderDTO;
import com.akido.orderservice.entities.Order;
import com.akido.orderservice.entities.User;
import com.akido.orderservice.repositories.OrderRepository;
import com.akido.orderservice.repositories.UserRepository;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;

    public OrderService(OrderRepository orderRepository, UserRepository userRepository) {
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
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

    public List<OrderDTO> getUserOrders(String username) {
        User user = userRepository.findByUsername(username);

        List<Order> all = orderRepository.findAllByUserId(user.getId());

        return all.stream()
                .map(order -> new OrderDTO(
                        order.getId(),
                        order.getUser().getId(),
                        order.getDescription(),
                        order.getStatus(),
                        order.getCreatedAt())).toList();
    }
}
