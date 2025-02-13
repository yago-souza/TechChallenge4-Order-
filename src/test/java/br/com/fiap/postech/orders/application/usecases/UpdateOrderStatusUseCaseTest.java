package br.com.fiap.postech.orders.application.usecases;

import br.com.fiap.postech.orders.domain.entities.Order;
import br.com.fiap.postech.orders.domain.entities.OrderItem;
import br.com.fiap.postech.orders.domain.enums.OrderStatus;
import br.com.fiap.postech.orders.domain.enums.PaymentMethod;
import br.com.fiap.postech.orders.infrastructure.API.CustomerGateway;
import br.com.fiap.postech.orders.infrastructure.API.ProductGateway;
import br.com.fiap.postech.orders.infrastructure.exception.InvalidStatusException;
import br.com.fiap.postech.orders.infrastructure.gateway.impl.OrderRepositoryGatewayImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class UpdateOrderStatusUseCaseTest {

    @Mock
    private CustomerGateway customerGateway;

    @Mock
    private OrderRepositoryGatewayImpl orderRepositoryGateway;

    @Mock
    private ProductGateway productGateway;

    @InjectMocks
    private UpdateOrderStatusUseCase updateOrderStatusUseCase;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testUpdateStatusFromOpenToPaid() {
        // Criação de IDs aleatórios para simular um cliente e um produto
        UUID customerId = UUID.randomUUID();
        UUID productId = UUID.randomUUID();

        // Criação de um item de pedido real
        OrderItem item = new OrderItem(UUID.randomUUID(), productId, 2, 50.0, 100.0);

        // Criação do pedido real
        Order order = new Order(
                UUID.randomUUID(),
                OrderStatus.OPEN,
                customerId,
                List.of(item),
                "Rua Exemplo, 123",
                100.0,
                PaymentMethod.CREDIT_CARD,
                LocalDateTime.now(),
                "ABC123",
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        when(orderRepositoryGateway.findById(order.getId())).thenReturn(order);
        when(orderRepositoryGateway.save(order)).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Order updatedOrder = updateOrderStatusUseCase.execute(order.getId(), OrderStatus.PAID);

        // Assert
        assertEquals(OrderStatus.PAID, updatedOrder.getStatus());
        verify(orderRepositoryGateway, times(1)).save(order);
    }

    @Test
    void testUpdateStatusFromPaidToProcessing() {
        // Criação de IDs aleatórios para simular um cliente e um produto
        UUID customerId = UUID.randomUUID();
        UUID productId = UUID.randomUUID();

        // Criação de um item de pedido real
        OrderItem item = new OrderItem(UUID.randomUUID(), productId, 2, 50.0, 100.0);

        // Criação do pedido real
        Order order = new Order(
                UUID.randomUUID(),
                OrderStatus.PAID,
                customerId,
                List.of(item),
                "Rua Exemplo, 123",
                100.0,
                PaymentMethod.CREDIT_CARD,
                LocalDateTime.now(),
                "ABC123",
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        when(orderRepositoryGateway.findById(order.getId())).thenReturn(order);
        when(orderRepositoryGateway.save(order)).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Order updatedOrder = updateOrderStatusUseCase.execute(order.getId(), OrderStatus.PROCESSING);

        // Assert
        assertEquals(OrderStatus.PROCESSING, updatedOrder.getStatus());
        verify(orderRepositoryGateway, times(1)).save(order);
    }

    @Test
    void testUpdateStatusFromProcessingToShipped() {
        UUID customerId = UUID.randomUUID();
        UUID productId = UUID.randomUUID();

        // Criação de um item de pedido real
        OrderItem item = new OrderItem(UUID.randomUUID(), productId, 2, 50.0, 100.0);

        // Criação do pedido real
        Order order = new Order(
                UUID.randomUUID(),
                OrderStatus.PROCESSING,
                customerId,
                List.of(item),
                "Rua Exemplo, 123",
                100.0,
                PaymentMethod.CREDIT_CARD,
                LocalDateTime.now(),
                "ABC123",
                LocalDateTime.now(),
                LocalDateTime.now()
        );


        when(orderRepositoryGateway.findById(order.getId())).thenReturn(order);
        when(orderRepositoryGateway.save(order)).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Order updatedOrder = updateOrderStatusUseCase.execute(order.getId(), OrderStatus.SHIPPED);

        // Assert
        assertEquals(OrderStatus.SHIPPED, updatedOrder.getStatus());
        verify(orderRepositoryGateway, times(1)).save(order);
    }

    @Test
    void testUpdateStatusFromShippedToDelivered() {
        UUID customerId = UUID.randomUUID();
        UUID productId = UUID.randomUUID();

        // Criação de um item de pedido real
        OrderItem item = new OrderItem(UUID.randomUUID(), productId, 2, 50.0, 100.0);

        // Criação do pedido real
        Order order = new Order(
                UUID.randomUUID(),
                OrderStatus.SHIPPED,
                customerId,
                List.of(item),
                "Rua Exemplo, 123",
                100.0,
                PaymentMethod.CREDIT_CARD,
                LocalDateTime.now(),
                "ABC123",
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        when(orderRepositoryGateway.findById(order.getId())).thenReturn(order);
        when(orderRepositoryGateway.save(order)).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Order updatedOrder = updateOrderStatusUseCase.execute(order.getId(), OrderStatus.DELIVERED);

        // Assert
        assertEquals(OrderStatus.DELIVERED, updatedOrder.getStatus());
        verify(orderRepositoryGateway, times(1)).save(order);
    }

    @Test
    void testUpdateStatusFromDeliveredToReturned() {
        UUID customerId = UUID.randomUUID();
        UUID productId = UUID.randomUUID();

        // Criação de um item de pedido real
        OrderItem item = new OrderItem(UUID.randomUUID(), productId, 2, 50.0, 100.0);

        // Criação do pedido real
        Order order = new Order(
                UUID.randomUUID(),
                OrderStatus.DELIVERED,
                customerId,
                List.of(item),
                "Rua Exemplo, 123",
                100.0,
                PaymentMethod.CREDIT_CARD,
                LocalDateTime.now(),
                "ABC123",
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        when(orderRepositoryGateway.findById(order.getId())).thenReturn(order);
        when(orderRepositoryGateway.save(order)).thenAnswer(invocation -> invocation.getArgument(0));

        // Assert
        assertThrows(InvalidStatusException.class, () -> updateOrderStatusUseCase.execute(order.getId(), OrderStatus.OPEN));
    }

    @Test
    void testUpdateStatusFromOpenToCanceled() {
        UUID customerId = UUID.randomUUID();
        UUID productId = UUID.randomUUID();

        // Criação de um item de pedido real
        OrderItem item = new OrderItem(UUID.randomUUID(), productId, 2, 50.0, 100.0);

        // Criação do pedido real
        Order order = new Order(
                UUID.randomUUID(),
                OrderStatus.OPEN,
                customerId,
                List.of(item),
                "Rua Exemplo, 123",
                100.0,
                PaymentMethod.CREDIT_CARD,
                LocalDateTime.now(),
                "ABC123",
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        when(orderRepositoryGateway.findById(order.getId())).thenReturn(order);
        when(orderRepositoryGateway.save(order)).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Order updatedOrder = updateOrderStatusUseCase.execute(order.getId(), OrderStatus.CANCELED);

        // Assert
        assertEquals(OrderStatus.CANCELED, updatedOrder.getStatus());
        verify(orderRepositoryGateway, times(1)).save(order);
    }

    @Test
    void testUpdateStatusFromCanceledToOpen() {
        UUID customerId = UUID.randomUUID();
        UUID productId = UUID.randomUUID();

        // Criação de um item de pedido real
        OrderItem item = new OrderItem(UUID.randomUUID(), productId, 2, 50.0, 100.0);

        // Criação do pedido real
        Order order = new Order(
                UUID.randomUUID(),
                OrderStatus.CANCELED,
                customerId,
                List.of(item),
                "Rua Exemplo, 123",
                100.0,
                PaymentMethod.CREDIT_CARD,
                LocalDateTime.now(),
                "ABC123",
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        when(orderRepositoryGateway.findById(order.getId())).thenReturn(order);
        when(orderRepositoryGateway.save(order)).thenReturn(order);

        // Assert
        assertThrows(InvalidStatusException.class, () -> updateOrderStatusUseCase.execute(order.getId(), OrderStatus.OPEN));
    }

    @Test
    void testUpdateStatusFromOpenToThrowsException() {
        UUID customerId = UUID.randomUUID();
        UUID productId = UUID.randomUUID();

        // Criação de um item de pedido real
        OrderItem item = new OrderItem(UUID.randomUUID(), productId, 2, 50.0, 100.0);

        // Criação do pedido real
        Order order = new Order(
                UUID.randomUUID(),
                OrderStatus.OPEN,
                customerId,
                List.of(item),
                "Rua Exemplo, 123",
                100.0,
                PaymentMethod.CREDIT_CARD,
                LocalDateTime.now(),
                "ABC123",
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        when(orderRepositoryGateway.findById(order.getId())).thenReturn(order);

        // Act & Assert
        assertThrows(InvalidStatusException.class, () -> {
            updateOrderStatusUseCase.execute(order.getId(), OrderStatus.OPEN);
        });

        verify(orderRepositoryGateway, never()).save(order);
    }

    @Test
    void testUpdateStatusWithSameStatusThrowsException() {
        UUID customerId = UUID.randomUUID();
        UUID productId = UUID.randomUUID();

        // Criação de um item de pedido real
        OrderItem item = new OrderItem(UUID.randomUUID(), productId, 2, 50.0, 100.0);

        // Criação do pedido real
        Order order = new Order(
                UUID.randomUUID(),
                OrderStatus.OPEN,
                customerId,
                List.of(item),
                "Rua Exemplo, 123",
                100.0,
                PaymentMethod.CREDIT_CARD,
                LocalDateTime.now(),
                "ABC123",
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        when(orderRepositoryGateway.findById(order.getId())).thenReturn(order);

        // Act & Assert
        assertThrows(InvalidStatusException.class, () -> {
            updateOrderStatusUseCase.execute(order.getId(), OrderStatus.OPEN);
        });

        verify(orderRepositoryGateway, never()).save(order);
    }

    @Test
    void testUpdateStatusFromDeliveredToShippedThrowsException() {
        UUID customerId = UUID.randomUUID();
        UUID productId = UUID.randomUUID();

        // Criação de um item de pedido real
        OrderItem item = new OrderItem(UUID.randomUUID(), productId, 2, 50.0, 100.0);

        // Criação do pedido real
        Order order = new Order(
                UUID.randomUUID(),
                OrderStatus.DELIVERED,
                customerId,
                List.of(item),
                "Rua Exemplo, 123",
                100.0,
                PaymentMethod.CREDIT_CARD,
                LocalDateTime.now(),
                "ABC123",
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        when(orderRepositoryGateway.findById(order.getId())).thenReturn(order);

        // Act & Assert
        assertThrows(InvalidStatusException.class, () -> {
            updateOrderStatusUseCase.execute(order.getId(), OrderStatus.SHIPPED);
        });

        verify(orderRepositoryGateway, never()).save(order);
    }

    @Test
    void testUpdateStatusFromReturnedThrowsException() {
        UUID customerId = UUID.randomUUID();
        UUID productId = UUID.randomUUID();

        // Criação de um item de pedido real
        OrderItem item = new OrderItem(UUID.randomUUID(), productId, 2, 50.0, 100.0);

        // Criação do pedido real
        Order order = new Order(
                UUID.randomUUID(),
                OrderStatus.RETURNED,
                customerId,
                List.of(item),
                "Rua Exemplo, 123",
                100.0,
                PaymentMethod.CREDIT_CARD,
                LocalDateTime.now(),
                "ABC123",
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        when(orderRepositoryGateway.findById(order.getId())).thenReturn(order);

        // Act & Assert
        assertThrows(InvalidStatusException.class, () -> {
            updateOrderStatusUseCase.execute(order.getId(), OrderStatus.OPEN);
        });

        verify(orderRepositoryGateway, never()).save(order);
    }
}