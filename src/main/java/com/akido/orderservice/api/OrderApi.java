package com.akido.orderservice.api;

import com.akido.orderservice.dto.CreateOrderRequestDTO;
import com.akido.orderservice.dto.OrderResponseDTO;
import com.akido.orderservice.dto.UpdateOrderStatusRequestDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.UUID;

@SecurityRequirement(name = "bearerAuth")
public interface OrderApi {

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
    Page<OrderResponseDTO> getAllOrders(Pageable pageable);

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
    Page<OrderResponseDTO> getUserOrders(Jwt jwt, Pageable pageable);

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
    void createOrder(Jwt jwt, CreateOrderRequestDTO description);

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
    void updateOrderStatus(UUID orderId, UpdateOrderStatusRequestDTO orderStatus);

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
    void deleteOrder(UUID orderId, Jwt jwt);
}
