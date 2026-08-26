package com.akido.orderservice.services;

import com.akido.orderservice.dto.UpdateOrderStatusRequestDTO;
import com.akido.orderservice.enums.Role;
import com.akido.orderservice.exceptions.NoPermissionException;
import com.akido.orderservice.exceptions.OrderNotFoundException;
import com.akido.orderservice.exceptions.UserNotFoundException;
import com.akido.orderservice.dto.OrderResponseDTO;
import com.akido.orderservice.entities.Order;
import com.akido.orderservice.entities.User;
import com.akido.orderservice.mappers.OrderMapper;
import com.akido.orderservice.repositories.OrderRepository;
import com.akido.orderservice.repositories.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final OrderMapper orderMapper;

    public OrderService(OrderRepository orderRepository, UserRepository userRepository, OrderMapper orderMapper) {
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
        this.orderMapper = orderMapper;
    }

    public Page<OrderResponseDTO> getAllOrders(Pageable pageable){

        return  orderRepository.findAll(getSortedPageable(pageable))
                .map(orderMapper::toDTO);
    }

    public Page<OrderResponseDTO> getUserOrders(String username, Pageable pageable) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(username));

        return orderRepository.findAllByUserId(user.getId(), getSortedPageable(pageable))
                .map(orderMapper::toDTO);
    }

    public void createOrder(String username, String description){
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(username));

        Order order = new Order();

        order.setDescription(description);
        order.setUser(user);
        orderRepository.save(order);
    }

    public void updateOrderStatus(UUID orderId, UpdateOrderStatusRequestDTO orderStatus) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(()->new OrderNotFoundException(orderId));

        order.setStatus(orderStatus.status());
        orderRepository.save(order);
    }

    public void deleteOrder(UUID orderId, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(username));
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException(orderId));

        boolean isOwner = user.getId().equals(order.getUser().getId());

        if (user.getRole() != Role.ADMIN && !isOwner){
            throw new NoPermissionException("User " + username + " has no permission for this action");
        }

        orderRepository.delete(order);
    }

    private Pageable getSortedPageable(Pageable unsortedPageable){
        return PageRequest.of(
                unsortedPageable.getPageNumber(),
                unsortedPageable.getPageSize(),
                Sort.by("createdAt").descending());
    }
}
