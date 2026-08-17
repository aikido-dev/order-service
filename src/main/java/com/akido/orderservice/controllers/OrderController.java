package com.akido.orderservice.controllers;

import com.akido.orderservice.dto.OrderDTO;
import com.akido.orderservice.services.OrderService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class OrderController {
    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping("/api/orders/all")
    public List<OrderDTO> ordersAll(){
        return orderService.getAllOrders();
    }
}
