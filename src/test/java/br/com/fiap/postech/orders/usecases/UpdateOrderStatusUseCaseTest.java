package br.com.fiap.postech.orders.usecases;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;
import java.util.UUID;

import br.com.fiap.postech.orders.domain.entities.Order;
import br.com.fiap.postech.orders.domain.enums.OrderStatus;
import br.com.fiap.postech.orders.infrastructure.exception.InvalidStatusException;
import br.com.fiap.postech.orders.infrastructure.persistence.repositories.OrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class UpdateOrderStatusUseCaseTest {

    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private UpdateOrderStatusUseCase updateOrderStatusUseCase;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this); // Inicializa os mocks
    }

    @Test
    void testUpdateStatusFromOpenToPaid() {
        UUID orderId = UUID.randomUUID();
        // Arrange
        Order order = new Order();
        order.setId(orderId);
        order.setStatus(OrderStatus.OPEN);

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));
        when(orderRepository.save(any(Order.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Order updatedOrder = updateOrderStatusUseCase.execute(orderId, OrderStatus.PAID);

        // Assert
        assertEquals(OrderStatus.PAID, updatedOrder.getStatus());
        verify(orderRepository, times(1)).save(order);
    }

    @Test
    void testUpdateStatusFromPaidToProcessing() {
        UUID orderId = UUID.randomUUID();
        // Arrange
        Order order = new Order();
        order.setId(orderId);
        order.setStatus(OrderStatus.PAID);

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));
        when(orderRepository.save(any(Order.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Order updatedOrder = updateOrderStatusUseCase.execute(orderId, OrderStatus.PROCESSING);

        // Assert
        assertEquals(OrderStatus.PROCESSING, updatedOrder.getStatus());
        verify(orderRepository, times(1)).save(order);
    }

    @Test
    void testUpdateStatusFromProcessingToShipped() {
        UUID orderId = UUID.randomUUID();
        // Arrange
        Order order = new Order();
        order.setId(orderId);
        order.setStatus(OrderStatus.PROCESSING);

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));
        when(orderRepository.save(any(Order.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Order updatedOrder = updateOrderStatusUseCase.execute(orderId, OrderStatus.SHIPPED);

        // Assert
        assertEquals(OrderStatus.SHIPPED, updatedOrder.getStatus());
        verify(orderRepository, times(1)).save(order);
    }

    @Test
    void testUpdateStatusFromShippedToDelivered() {
        UUID orderId = UUID.randomUUID();
        // Arrange
        Order order = new Order();
        order.setId(orderId);
        order.setStatus(OrderStatus.SHIPPED);

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));
        when(orderRepository.save(any(Order.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Order updatedOrder = updateOrderStatusUseCase.execute(orderId, OrderStatus.DELIVERED);

        // Assert
        assertEquals(OrderStatus.DELIVERED, updatedOrder.getStatus());
        verify(orderRepository, times(1)).save(order);
    }

    @Test
    void testUpdateStatusFromDeliveredToReturned() {
        UUID orderId = UUID.randomUUID();
        // Arrange
        Order order = new Order();
        order.setId(orderId);
        order.setStatus(OrderStatus.DELIVERED);

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));
        when(orderRepository.save(any(Order.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Order updatedOrder = updateOrderStatusUseCase.execute(orderId, OrderStatus.RETURNED);

        // Assert
        assertEquals(OrderStatus.RETURNED, updatedOrder.getStatus());
        verify(orderRepository, times(1)).save(order);
    }

    @Test
    void testUpdateStatusFromOpenToCanceled() {
        UUID orderId = UUID.randomUUID();
        // Arrange
        Order order = new Order();
        order.setId(orderId);
        order.setStatus(OrderStatus.OPEN);

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));
        when(orderRepository.save(any(Order.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Order updatedOrder = updateOrderStatusUseCase.execute(orderId, OrderStatus.CANCELED);

        // Assert
        assertEquals(OrderStatus.CANCELED, updatedOrder.getStatus());
        verify(orderRepository, times(1)).save(order);
    }

    @Test
    void testUpdateStatusFromCanceledToOpen() {
        UUID orderId = UUID.randomUUID();
        // Arrange
        Order order = new Order();
        order.setId(orderId);
        order.setStatus(OrderStatus.CANCELED);

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));
        when(orderRepository.save(any(Order.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Order updatedOrder = updateOrderStatusUseCase.execute(orderId, OrderStatus.OPEN);

        // Assert
        assertEquals(OrderStatus.OPEN, updatedOrder.getStatus());
        verify(orderRepository, times(1)).save(order);
    }

    @Test
    void testUpdateStatusWithSameStatusThrowsException() {
        UUID orderId = UUID.randomUUID();
        // Arrange
        Order order = new Order();
        order.setId(orderId);
        order.setStatus(OrderStatus.OPEN);

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));

        // Act & Assert
        assertThrows(InvalidStatusException.class, () -> {
            updateOrderStatusUseCase.execute(orderId, OrderStatus.OPEN);
        });

        verify(orderRepository, never()).save(any(Order.class));
    }

    @Test
    void testUpdateStatusFromDeliveredToShippedThrowsException() {
        UUID orderId = UUID.randomUUID();
        // Arrange
        Order order = new Order();
        order.setId(orderId);
        order.setStatus(OrderStatus.DELIVERED);

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));

        // Act & Assert
        assertThrows(InvalidStatusException.class, () -> {
            updateOrderStatusUseCase.execute(orderId, OrderStatus.SHIPPED);
        });

        verify(orderRepository, never()).save(any(Order.class));
    }

    @Test
    void testUpdateStatusFromReturnedThrowsException() {
        UUID orderId = UUID.randomUUID();
        // Arrange
        Order order = new Order();
        order.setId(orderId);
        order.setStatus(OrderStatus.RETURNED);

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));

        // Act & Assert
        assertThrows(InvalidStatusException.class, () -> {
            updateOrderStatusUseCase.execute(orderId, OrderStatus.OPEN);
        });

        verify(orderRepository, never()).save(any(Order.class));
    }

    @Test
    void testUpdateStatusWithInvalidStatusThrowsException() {
        UUID orderId = UUID.randomUUID();
        // Arrange
        Order order = new Order();
        order.setId(orderId);
        order.setStatus(OrderStatus.OPEN);

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));

        // Act & Assert
        assertThrows(InvalidStatusException.class, () -> {
            updateOrderStatusUseCase.execute(orderId, null); // Status inválido
        });

        verify(orderRepository, never()).save(any(Order.class));
    }
}