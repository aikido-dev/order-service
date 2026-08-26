package com.akido.orderservice.controllers;

import com.akido.orderservice.dto.CreateOrderRequestDTO;
import com.akido.orderservice.dto.OrderResponseDTO;
import com.akido.orderservice.dto.UpdateOrderStatusRequestDTO;
import com.akido.orderservice.services.OrderService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    public Page<OrderResponseDTO> getAllOrders(Pageable pageable) {
        return orderService.getAllOrders(pageable);
    }

    @GetMapping("/api/orders")
    public Page<OrderResponseDTO> getUserOrders(
            @AuthenticationPrincipal Jwt jwt,
            Pageable pageable){
        return orderService.getUserOrders(jwt.getSubject(), pageable);
    }

    @PostMapping("/api/orders")
    @ResponseStatus(HttpStatus.CREATED)
    public void createOrder(
            @AuthenticationPrincipal Jwt jwt,
            @RequestBody @Valid CreateOrderRequestDTO description){

        orderService.createOrder(jwt.getSubject(), description.description());
    }

    @PutMapping("/api/orders/{id}")
    public void updateOrderStatus(
            @PathVariable(name = "id") UUID orderId,
            @RequestBody @Valid UpdateOrderStatusRequestDTO orderStatus){
        orderService.updateOrderStatus(orderId, orderStatus);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/api/orders/{id}")
    public void deleteOrder(@PathVariable(name="id") UUID orderId,
                            @AuthenticationPrincipal Jwt jwt){
        orderService.deleteOrder(orderId, jwt.getSubject());

    }


}
