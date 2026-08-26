package com.akido.orderservice.mappers;

import com.akido.orderservice.dto.OrderResponseDTO;
import com.akido.orderservice.entities.Order;
import org.springframework.stereotype.Component;

@Component
public class OrderMapper {

    public OrderResponseDTO toDTO(Order order){
        return new OrderResponseDTO(
                order.getId(),
                order.getUser().getId(),
                order.getDescription(),
                order.getStatus(),
                order.getCreatedAt()
        );
    }
}
