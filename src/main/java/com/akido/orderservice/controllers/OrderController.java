package com.akido.orderservice.controllers;

import com.akido.orderservice.dto.CreateOrderRequestDTO;
import com.akido.orderservice.dto.OrderDTO;
import com.akido.orderservice.dto.UpdateOrderStatusDTO;
import com.akido.orderservice.services.OrderService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
public class OrderController {
    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping("/api/orders/all")
    public List<OrderDTO> getAllOrders() {
        return orderService.getAllOrders();
    }

    @GetMapping("/api/orders")
    public List<OrderDTO> getUserOrders(
            @AuthenticationPrincipal Jwt jwt){
        return orderService.getUserOrders(jwt.getSubject());
    }

    @PostMapping("/api/orders")
    @ResponseStatus(HttpStatus.CREATED)
    public void createOrder(
            @AuthenticationPrincipal Jwt jwt,
            @RequestBody @Valid CreateOrderRequestDTO description){

        orderService.createOrder(jwt.getSubject(), description.description());
    }

    @PutMapping("/api/orders/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void updateOrderStatus(
            @PathVariable(name = "id") UUID orderId,
            @RequestBody @Valid UpdateOrderStatusDTO orderStatus){
        orderService.updateOrderStatus(orderId, orderStatus);
    }


}
