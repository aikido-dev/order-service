package com.akido.orderservice.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.UUID;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class OrderNotFoundException extends RuntimeException {

    public OrderNotFoundException(UUID orderId) {
        super("Order with id: " + orderId + " does not exist.");
    }
}
