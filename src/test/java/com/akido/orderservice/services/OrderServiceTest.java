package com.akido.orderservice.services;

import com.akido.orderservice.dto.OrderResponseDTO;
import com.akido.orderservice.dto.UpdateOrderStatusRequestDTO;
import com.akido.orderservice.entities.Order;
import com.akido.orderservice.entities.User;
import com.akido.orderservice.enums.Role;
import com.akido.orderservice.enums.Status;
import com.akido.orderservice.exceptions.NoPermissionException;
import com.akido.orderservice.exceptions.OrderNotFoundException;
import com.akido.orderservice.exceptions.UserNotFoundException;
import com.akido.orderservice.mappers.OrderMapper;
import com.akido.orderservice.repositories.OrderRepository;
import com.akido.orderservice.repositories.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {
    @Mock
    private OrderRepository orderRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private OrderMapper orderMapper;

    @InjectMocks
    private OrderService orderService;

    @Test
    void getAllOrders_shouldReturnMappedOrders() {
        // Arrange
        Order order1 = new Order();
        Order order2 = new Order();

        OrderResponseDTO dto1 = new OrderResponseDTO(
                UUID.randomUUID(),
                UUID.randomUUID(),
                "description1",
                Status.CREATED,
                LocalDateTime.now());

        OrderResponseDTO dto2 = new OrderResponseDTO(
                UUID.randomUUID(),
                UUID.randomUUID(),
                "description2",
                Status.CREATED,
                LocalDateTime.now());

        int pageNumber = 0;
        int pageSize = 20;

        ArgumentCaptor<Pageable> captor = ArgumentCaptor.forClass(Pageable.class);

        when(orderRepository.findAll(any(Pageable.class)))
                .thenReturn(new PageImpl<>(List.of(order1, order2)));

        when(orderMapper.toDTO(order1))
                .thenReturn(dto1);

        when(orderMapper.toDTO(order2))
                .thenReturn(dto2);

        Pageable pageable = PageRequest.of(pageNumber, pageSize);

        // Act
        Page<OrderResponseDTO> orders = orderService.getAllOrders(pageable);

        // Assert
        List<OrderResponseDTO> items = List.of(dto1, dto2);
        Page<OrderResponseDTO> expected = new PageImpl<>(items);

        assertEquals(expected, orders);
        verify(orderRepository).findAll(captor.capture());
        verify(orderMapper).toDTO(order1);
        verify(orderMapper).toDTO(order2);

        Pageable result = captor.getValue();
        assertEquals(pageNumber, result.getPageNumber());
        assertEquals(pageSize, result.getPageSize());
        assertEquals(Sort.by("createdAt").descending(), result.getSort());
    }

    @Test
    void getUserOrders_shouldReturnMappedOrders() {
        // Arrange
        Order order1 = new Order();
        Order order2 = new Order();

        OrderResponseDTO dto1 = new OrderResponseDTO(
                UUID.randomUUID(),
                UUID.randomUUID(),
                "description1",
                Status.CREATED,
                LocalDateTime.now());

        OrderResponseDTO dto2 = new OrderResponseDTO(
                UUID.randomUUID(),
                UUID.randomUUID(),
                "description2",
                Status.CREATED,
                LocalDateTime.now());

        String username = "username";
        UUID userId = UUID.randomUUID();
        int pageNumber = 0;
        int pageSize = 20;

        User user = new User();
        user.setUsername(username);
        user.setId(userId);

        ArgumentCaptor<Pageable> captor = ArgumentCaptor.forClass(Pageable.class);

        when(userRepository.findByUsername(username))
                .thenReturn(Optional.of(user));

        when(orderRepository.findAllByUserId(eq(userId), any(Pageable.class)))
                .thenReturn(new PageImpl<>(List.of(order1, order2)));

        when(orderMapper.toDTO(order1))
                .thenReturn(dto1);

        when(orderMapper.toDTO(order2))
                .thenReturn(dto2);

        Pageable pageable = PageRequest.of(pageNumber, pageSize);

        // Act
        Page<OrderResponseDTO> orders = orderService.getUserOrders(username, pageable);

        // Assert
        Page<OrderResponseDTO> expected = new PageImpl<>(List.of(dto1, dto2));

        assertEquals(expected, orders);
        verify(orderRepository).findAllByUserId(eq(userId), captor.capture());
        verify(userRepository).findByUsername(username);

        Pageable result = captor.getValue();
        assertEquals(pageNumber, result.getPageNumber());
        assertEquals(pageSize, result.getPageSize());
        assertEquals(Sort.by("createdAt").descending(), result.getSort());
    }

    @Test
    void getUserOrders_whenUserNotFound_shouldThrowUserNotFoundException() {
        // Arrange
        String username = "username";

        when(userRepository.findByUsername(any(String.class)))
                .thenReturn(Optional.empty());

        // Assert
        assertThrows(UserNotFoundException.class,
                () -> orderService.getUserOrders(username, PageRequest.of(0, 20)));

        verify(userRepository).findByUsername(username);
        verify(orderRepository, never()).findAllByUserId(any(), any());
    }

    @Test
    void createOrder_shouldSaveOrder() {
        // Arrange
        String description = "description";
        String username = "username";

        User user = new User();
        user.setUsername(username);
        user.setId(UUID.randomUUID());

        ArgumentCaptor<Order> captor = ArgumentCaptor.forClass(Order.class);

        when(userRepository.findByUsername(username))
                .thenReturn(Optional.of(user));

        // Act
        orderService.createOrder(username, description);

        // Assert
        verify(orderRepository).save(captor.capture());

        Order order = captor.getValue();
        assertEquals(user, order.getUser());
        assertEquals(description, order.getDescription());
        assertEquals(Status.CREATED, order.getStatus());
    }

    @Test
    void createOrder_whenUserDoesNotExist_shouldThrowUserNotFoundException() {
        // Arrange
        String description = "description";
        String username = "username";

        when(userRepository.findByUsername(username))
                .thenReturn(Optional.empty());

        // Assert
        assertThrows(UserNotFoundException.class,
                () -> orderService.createOrder(username, description));
        verify(orderRepository, never()).save(any());
    }

    @Test
    void updateOrderStatus_shouldUpdateOrder() {
        // Arrange
        UUID orderId = UUID.randomUUID();
        UpdateOrderStatusRequestDTO newStatus = new UpdateOrderStatusRequestDTO(Status.IN_PROGRESS);

        Order order = new Order();
        order.setId(orderId);
        order.setStatus(Status.CREATED);

        when(orderRepository.findById(orderId))
                .thenReturn(Optional.of(order));

        // Act
        orderService.updateOrderStatus(orderId, newStatus);

        // Assert
        verify(orderRepository).save(order);

        assertEquals(newStatus.status(), order.getStatus());
    }

    @Test
    void updateOrderStatus_whenOrderDoesNotExist_shouldThrowOrderNotFoundException() {
        // Arrange
        UUID orderId = UUID.randomUUID();
        UpdateOrderStatusRequestDTO newStatus = new UpdateOrderStatusRequestDTO(Status.IN_PROGRESS);

        when(orderRepository.findById(orderId))
                .thenReturn(Optional.empty());

        // Assert
        assertThrows(OrderNotFoundException.class,
                () -> orderService.updateOrderStatus(orderId, newStatus));
        verify(orderRepository, never()).save(any());
    }

    @Test
    void deleteOrder_whenOwnerDeletes_shouldDeleteOrder() {
        // Arrange
        UUID orderId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        String username = "username";

        User user = new User();
        user.setUsername(username);
        user.setId(userId);
        user.setRole(Role.USER);

        Order order = new Order();
        order.setUser(user);
        order.setId(orderId);

        when(userRepository.findByUsername(username))
                .thenReturn(Optional.of(user));

        when(orderRepository.findById(orderId))
                .thenReturn(Optional.of(order));

        // Act
        orderService.deleteOrder(orderId, username);

        // Assert
        verify(orderRepository).delete(order);
    }

    @Test
    void deleteOrder_whenAdminDeletes_shouldDeleteOrder() {
        // Arrange
        UUID orderId = UUID.randomUUID();
        String username = "username";

        User user = new User();
        user.setUsername(username);
        user.setId(UUID.randomUUID());
        user.setRole(Role.ADMIN);

        Order order = new Order();
        order.setUser(new User());
        order.setId(orderId);

        when(userRepository.findByUsername(username))
                .thenReturn(Optional.of(user));

        when(orderRepository.findById(orderId))
                .thenReturn(Optional.of(order));

        // Act
        orderService.deleteOrder(orderId, username);

        // Assert
        verify(orderRepository).delete(order);
    }

    @Test
    void deleteOrder_whenUserIsNeitherOwnerNorAdmin_shouldThrowNoPermissionException() {
        // Arrange
        UUID orderId = UUID.randomUUID();
        String username = "username";

        User user = new User();
        user.setUsername(username);
        user.setId(UUID.randomUUID());
        user.setRole(Role.USER);

        Order order = new Order();
        order.setUser(new User());
        order.setId(orderId);

        when(userRepository.findByUsername(username))
                .thenReturn(Optional.of(user));

        when(orderRepository.findById(orderId))
                .thenReturn(Optional.of(order));

        // Assert
        assertThrows(NoPermissionException.class,
                () -> orderService.deleteOrder(orderId, username));
        verify(orderRepository, never()).delete(any());
    }

    @Test
    void deleteOrder_whenUserDoesNotExist_shouldThrowUserNotFoundException() {
        // Arrange
        UUID orderId = UUID.randomUUID();
        String username = "username";

        when(userRepository.findByUsername(username))
                .thenReturn(Optional.empty());

        // Assert
        assertThrows(UserNotFoundException.class,
                () -> orderService.deleteOrder(orderId, username));

        verify(orderRepository, never()).delete(any());
        verify(orderRepository, never()).findById(any());
    }

    @Test
    void deleteOrder_whenOrderDoesNotExist_shouldThrowOrderNotFoundException() {
        // Arrange
        UUID orderId = UUID.randomUUID();
        String username = "username";

        when(userRepository.findByUsername(username))
                .thenReturn(Optional.of(new User()));

        when(orderRepository.findById(orderId))
                .thenReturn(Optional.empty());

        // Assert
        assertThrows(OrderNotFoundException.class,
                () -> orderService.deleteOrder(orderId, username));
        verify(orderRepository, never()).delete(any());
    }
}
