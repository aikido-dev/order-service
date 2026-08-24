package com.akido.orderservice.services;

import com.akido.orderservice.dto.UpdateOrderStatusDTO;
import com.akido.orderservice.enums.Role;
import com.akido.orderservice.exceptions.NoPermissionException;
import com.akido.orderservice.exceptions.OrderNotFoundException;
import com.akido.orderservice.exceptions.UserNotFoundException;
import com.akido.orderservice.dto.OrderDTO;
import com.akido.orderservice.entities.Order;
import com.akido.orderservice.entities.User;
import com.akido.orderservice.repositories.OrderRepository;
import com.akido.orderservice.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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
        User user = userRepository.findByUsername(username).orElse(null);

        List<Order> all = orderRepository.findAllByUserId(user.getId());

        return all.stream()
                .map(order -> new OrderDTO(
                        order.getId(),
                        order.getUser().getId(),
                        order.getDescription(),
                        order.getStatus(),
                        order.getCreatedAt())).toList();
    }

    public void createOrder(String username, String description){
        User user = userRepository.findByUsername(username).orElse(null);

        if (user != null){
            Order order = new Order();

            order.setDescription(description);
            order.setUser(user);
            orderRepository.save(order);
        }
        else {
            throw new UserNotFoundException(username);
        }

    }

    public void updateOrderStatus(UUID orderId, UpdateOrderStatusDTO orderStatus) {
        Order order = orderRepository.findById(orderId).orElse(null);
        if (order != null){
            order.setStatus(orderStatus.status());
            orderRepository.save(order);
        }else {
            throw new OrderNotFoundException(orderId);
        }
    }

    public void deleteOrder(UUID orderId, String username) {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new UserNotFoundException(username));
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new OrderNotFoundException(orderId));

        boolean isOwner = user.getId().equals(order.getUser().getId());

        if (user.getRole() != Role.ADMIN || !isOwner){
            throw new NoPermissionException("User " + username + " has no permission for this action");
        }

        orderRepository.delete(order);
    }
}
