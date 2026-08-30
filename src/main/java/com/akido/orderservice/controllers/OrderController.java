package com.akido.orderservice.controllers;

import com.akido.orderservice.dto.CreateOrderRequestDTO;
import com.akido.orderservice.dto.OrderResponseDTO;
import com.akido.orderservice.dto.UpdateOrderStatusRequestDTO;
import com.akido.orderservice.services.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@SecurityRequirement(name = "bearerAuth")
@RestController
public class OrderController {
    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @Operation(
            summary = "Получить все заказы.",
            description = "Возвращает все заказы согласно пагинации. " +
                    "Результат отсортирован от новых к старым. " +
                    "Доступен только администраторам."
    )
    @ApiResponse(
            responseCode = "401",
            description = "Аутентификация не пройдена."
    )
    @ApiResponse(
            responseCode = "403",
            description = "Недостаточно прав для этой операции."
    )
    @GetMapping("/api/orders/all")
    public Page<OrderResponseDTO> getAllOrders(Pageable pageable) {
        return orderService.getAllOrders(pageable);
    }

    @Operation(
            summary = "Получить все заказы текущего пользователя.",
            description = "Возвращает все заказы для текущего пользователя согласно пагинации. " +
                    "Результат отсортирован от новых к старым. " +
                    "Доступен только аутентифицированным пользователям."
    )
    @ApiResponse(
            responseCode = "401",
            description = "Аутентификация не пройдена."
    )
    @ApiResponse(
            responseCode = "404",
            description = "Текущий пользователь не найден."
    )
    @GetMapping("/api/orders")
    public Page<OrderResponseDTO> getUserOrders(
            @AuthenticationPrincipal Jwt jwt,
            Pageable pageable){
        return orderService.getUserOrders(jwt.getSubject(), pageable);
    }

    @Operation(
            summary = "Создать новый заказ.",
            description = "Создает новый заказ со статусом CREATED. " +
                    "Описание заказа не может быть пустым. " +
                    "Доступен только аутентифицированным пользователям."
    )
    @ApiResponse(
            responseCode = "401",
            description = "Аутентификация не пройдена."
    )
    @ApiResponse(
            responseCode = "201",
            description = "Заказ успешно создан."
    )
    @ApiResponse(
            responseCode = "400",
            description = "Описание заказа не прошло валидацию."
    )
    @PostMapping("/api/orders")
    @ResponseStatus(HttpStatus.CREATED)
    public void createOrder(
            @AuthenticationPrincipal Jwt jwt,
            @RequestBody @Valid CreateOrderRequestDTO description){

        orderService.createOrder(jwt.getSubject(), description.description());
    }

    @Operation(
            summary = "Обновить статус заказа.",
            description = "Обновляет статус существующего заказа. " +
                    "Статус должен соответствовать одному из существующих вариантов. " +
                    "Доступен только администраторам."
    )
    @ApiResponse(
            responseCode = "401",
            description = "Аутентификация не пройдена."
    )
    @ApiResponse(
            responseCode = "200",
            description = "Статус успешно обновлен."
    )
    @ApiResponse(
            responseCode = "403",
            description = "Недостаточно прав для этой операции."
    )
    @ApiResponse(
            responseCode = "404",
            description = "Заказ с таким id не был найден."
    )
    @ApiResponse(
            responseCode = "400",
            description = "Идентификатор заказа или статус не прошли валидацию."
    )
    @PutMapping("/api/orders/{id}")
    public void updateOrderStatus(
            @PathVariable(name = "id") UUID orderId,
            @RequestBody @Valid UpdateOrderStatusRequestDTO orderStatus){
        orderService.updateOrderStatus(orderId, orderStatus);
    }

    @Operation(
            summary = "Удалить заказ.",
            description = "Удаляет заказ. " +
                    "Доступен всем администраторам и владельцу данного заказа."
    )
    @ApiResponse(
            responseCode = "401",
            description = "Аутентификация не пройдена."
    )
    @ApiResponse(
            responseCode = "204",
            description = "Заказ успешно удален."
    )
    @ApiResponse(
            responseCode = "403",
            description = "Недостаточно прав для этой операции."
    )
    @ApiResponse(
            responseCode = "404",
            description = "Заказ или текущий пользователь не найден."
    )
    @ApiResponse(
            responseCode = "400",
            description = "Идентификатор заказа не прошел валидацию."
    )
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/api/orders/{id}")
    public void deleteOrder(@PathVariable(name="id") UUID orderId,
                            @AuthenticationPrincipal Jwt jwt){
        orderService.deleteOrder(orderId, jwt.getSubject());
    }
}
