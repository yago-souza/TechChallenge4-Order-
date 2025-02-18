package br.com.fiap.postech.orders.application.usecases;

import br.com.fiap.postech.orders.domain.entities.Address;
import br.com.fiap.postech.orders.domain.entities.Order;
import br.com.fiap.postech.orders.domain.entities.OrderItem;
import br.com.fiap.postech.orders.domain.enums.OrderStatus;
import br.com.fiap.postech.orders.domain.enums.PaymentMethod;
import br.com.fiap.postech.orders.infrastructure.api.CustomerGateway;
import br.com.fiap.postech.orders.infrastructure.api.ProductGateway;
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
        OrderItem item = new OrderItem(UUID.randomUUID(), productId, 2, 50.0);

        // Criação do pedido real
        Order order = new Order(
                OrderStatus.OPEN,
                customerId,
                List.of(item),
                new Address("Rua teste",
                        "123",
                        "Teste",
                        "Bairro teste",
                        "Cidade Teste",
                        "Estado Teste",
                        "Pais Teste",
                        "12345-678"),
                PaymentMethod.CREDIT_CARD,
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
        OrderItem item = new OrderItem(UUID.randomUUID(), productId, 2, 50.0);

        // Criação do pedido real
        Order order = new Order(
                OrderStatus.PAID,
                customerId,
                List.of(item),
                new Address("Rua teste",
                        "123",
                        "Teste",
                        "Bairro teste",
                        "Cidade Teste",
                        "Estado Teste",
                        "Pais Teste",
                        "12345-678"),
                PaymentMethod.CREDIT_CARD,
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
        OrderItem item = new OrderItem(UUID.randomUUID(), productId, 2, 50.0);

        // Criação do pedido real
        Order order = new Order(
                OrderStatus.PROCESSING,
                customerId,
                List.of(item),
                new Address("Rua teste",
                        "123",
                        "Teste",
                        "Bairro teste",
                        "Cidade Teste",
                        "Estado Teste",
                        "Pais Teste",
                        "12345-678"),
                PaymentMethod.CREDIT_CARD,
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
        OrderItem item = new OrderItem(UUID.randomUUID(), productId, 2, 50.0);

        // Criação do pedido real
        Order order = new Order(
                OrderStatus.SHIPPED,
                customerId,
                List.of(item),
                new Address("Rua teste",
                        "123",
                        "Teste",
                        "Bairro teste",
                        "Cidade Teste",
                        "Estado Teste",
                        "Pais Teste",
                        "12345-678"),
                PaymentMethod.CREDIT_CARD,
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
        OrderItem item = new OrderItem(UUID.randomUUID(), productId, 2, 50.0);

        // Criação do pedido real
        Order order = new Order(
                OrderStatus.DELIVERED,
                customerId,
                List.of(item),
                new Address("Rua teste",
                        "123",
                        "Teste",
                        "Bairro teste",
                        "Cidade Teste",
                        "Estado Teste",
                        "Pais Teste",
                        "12345-678"),
                PaymentMethod.CREDIT_CARD,
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        when(orderRepositoryGateway.findById(order.getId())).thenReturn(order);
        when(orderRepositoryGateway.save(order)).thenAnswer(invocation -> invocation.getArgument(0));

        // Assert
        UUID orderId = order.getId();
        assertThrows(InvalidStatusException.class, () -> updateOrderStatusUseCase.execute(orderId, OrderStatus.OPEN));
    }

    @Test
    void testUpdateStatusFromOpenToCanceled() {
        UUID customerId = UUID.randomUUID();
        UUID productId = UUID.randomUUID();

        // Criação de um item de pedido real
        OrderItem item = new OrderItem(UUID.randomUUID(), productId, 2, 50.0);

        // Criação do pedido real
        Order order = new Order(
                OrderStatus.OPEN,
                customerId,
                List.of(item),
                new Address("Rua teste",
                        "123",
                        "Teste",
                        "Bairro teste",
                        "Cidade Teste",
                        "Estado Teste",
                        "Pais Teste",
                        "12345-678"),
                PaymentMethod.CREDIT_CARD,
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
        OrderItem item = new OrderItem(UUID.randomUUID(), productId, 2, 50.0);

        // Criação do pedido real
        Order order = new Order(
                OrderStatus.CANCELED,
                customerId,
                List.of(item),
                new Address("Rua teste",
                        "123",
                        "Teste",
                        "Bairro teste",
                        "Cidade Teste",
                        "Estado Teste",
                        "Pais Teste",
                        "12345-678"),
                PaymentMethod.CREDIT_CARD,
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        when(orderRepositoryGateway.findById(order.getId())).thenReturn(order);
        when(orderRepositoryGateway.save(order)).thenReturn(order);

        // Assert
        UUID orderId = order.getId();
        assertThrows(InvalidStatusException.class, () -> updateOrderStatusUseCase.execute(orderId, OrderStatus.OPEN));
    }

    @Test
    void testUpdateStatusWithSameStatusThrowsException() {
        UUID customerId = UUID.randomUUID();
        UUID productId = UUID.randomUUID();

        // Criação de um item de pedido real
        OrderItem item = new OrderItem(UUID.randomUUID(), productId, 2, 50.0);

        // Criação do pedido real
        Order order = new Order(
                OrderStatus.OPEN,
                customerId,
                List.of(item),
                new Address("Rua teste",
                        "123",
                        "Teste",
                        "Bairro teste",
                        "Cidade Teste",
                        "Estado Teste",
                        "Pais Teste",
                        "12345-678"),
                PaymentMethod.CREDIT_CARD,
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        when(orderRepositoryGateway.findById(order.getId())).thenReturn(order);

        // Act & Assert
        UUID orderId = order.getId();
        assertThrows(InvalidStatusException.class, () -> {
            updateOrderStatusUseCase.execute(orderId, OrderStatus.OPEN);
        });

        verify(orderRepositoryGateway, never()).save(order);
    }

    @Test
    void testUpdateStatusFromDeliveredToShippedThrowsException() {
        UUID customerId = UUID.randomUUID();
        UUID productId = UUID.randomUUID();

        // Criação de um item de pedido real
        OrderItem item = new OrderItem(UUID.randomUUID(), productId, 2, 50.0);

        // Criação do pedido real
        Order order = new Order(
                OrderStatus.DELIVERED,
                customerId,
                List.of(item),
                new Address("Rua teste",
                        "123",
                        "Teste",
                        "Bairro teste",
                        "Cidade Teste",
                        "Estado Teste",
                        "Pais Teste",
                        "12345-678"),
                PaymentMethod.CREDIT_CARD,
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        when(orderRepositoryGateway.findById(order.getId())).thenReturn(order);

        // Act & Assert
        UUID orderId = order.getId();
        assertThrows(InvalidStatusException.class, () -> {
            updateOrderStatusUseCase.execute(orderId, OrderStatus.SHIPPED);
        });

        verify(orderRepositoryGateway, never()).save(order);
    }

    @Test
    void testUpdateStatusFromReturnedThrowsException() {
        UUID customerId = UUID.randomUUID();
        UUID productId = UUID.randomUUID();

        // Criação de um item de pedido real
        OrderItem item = new OrderItem(UUID.randomUUID(), productId, 2, 50.0);

        // Criação do pedido real
        Order order = new Order(
                OrderStatus.RETURNED,
                customerId,
                List.of(item),
                new Address("Rua teste",
                        "123",
                        "Teste",
                        "Bairro teste",
                        "Cidade Teste",
                        "Estado Teste",
                        "Pais Teste",
                        "12345-678"),
                PaymentMethod.CREDIT_CARD,
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        when(orderRepositoryGateway.findById(order.getId())).thenReturn(order);

        // Act & Assert
        UUID orderId = order.getId();
        assertThrows(InvalidStatusException.class, () -> {
            updateOrderStatusUseCase.execute(orderId, OrderStatus.OPEN);
        });

        verify(orderRepositoryGateway, never()).save(order);
    }

    @Test
    void testUpdateStatusFromOpenToInvalidStatusThrowsException() {
        UUID customerId = UUID.randomUUID();
        UUID productId = UUID.randomUUID();

        // Criação de um item de pedido real
        OrderItem item = new OrderItem(UUID.randomUUID(), productId, 2, 50.0);

        // Criação do pedido real
        Order order = new Order(
                OrderStatus.OPEN,
                customerId,
                List.of(item),
                new Address("Rua teste",
                        "123",
                        "Teste",
                        "Bairro teste",
                        "Cidade Teste",
                        "Estado Teste",
                        "Pais Teste",
                        "12345-678"),
                PaymentMethod.CREDIT_CARD,
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        when(orderRepositoryGateway.findById(order.getId())).thenReturn(order);

        // Teste para transição inválida de OPEN para qualquer status diferente de PAID ou CANCELED
        UUID orderId = order.getId();
        assertThrows(InvalidStatusException.class, () -> {
            updateOrderStatusUseCase.execute(orderId, OrderStatus.PROCESSING); // Transição inválida
        });

        verify(orderRepositoryGateway, never()).save(order);
    }

    @Test
    void testUpdateStatusFromPaidToInvalidStatusThrowsException() {
        UUID customerId = UUID.randomUUID();
        UUID productId = UUID.randomUUID();

        // Criação de um item de pedido real
        OrderItem item = new OrderItem(UUID.randomUUID(), productId, 2, 50.0);

        // Criação do pedido real
        Order order = new Order(
                OrderStatus.PAID,
                customerId,
                List.of(item),
                new Address("Rua teste",
                        "123",
                        "Teste",
                        "Bairro teste",
                        "Cidade Teste",
                        "Estado Teste",
                        "Pais Teste",
                        "12345-678"),
                PaymentMethod.CREDIT_CARD,
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        when(orderRepositoryGateway.findById(order.getId())).thenReturn(order);

        // Teste para transição inválida de PAID para qualquer status diferente de PROCESSING ou CANCELED
        UUID orderId = order.getId();
        assertThrows(InvalidStatusException.class, () -> {
            updateOrderStatusUseCase.execute(orderId, OrderStatus.OPEN); // Transição inválida
        });

        verify(orderRepositoryGateway, never()).save(order);
    }

    @Test
    void testUpdateStatusFromProcessingToInvalidStatusThrowsException() {
        UUID customerId = UUID.randomUUID();
        UUID productId = UUID.randomUUID();

        // Criação de um item de pedido real
        OrderItem item = new OrderItem(UUID.randomUUID(), productId, 2, 50.0);

        // Criação do pedido real
        Order order = new Order(
                OrderStatus.PROCESSING,
                customerId,
                List.of(item),
                new Address("Rua teste",
                        "123",
                        "Teste",
                        "Bairro teste",
                        "Cidade Teste",
                        "Estado Teste",
                        "Pais Teste",
                        "12345-678"),
                PaymentMethod.CREDIT_CARD,
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        when(orderRepositoryGateway.findById(order.getId())).thenReturn(order);

        // Teste para transição inválida de PROCESSING para qualquer status diferente de SHIPPED ou CANCELED
        UUID orderId = order.getId();
        assertThrows(InvalidStatusException.class, () -> {
            updateOrderStatusUseCase.execute(orderId, OrderStatus.DELIVERED); // Transição inválida
        });

        verify(orderRepositoryGateway, never()).save(order);
    }

    @Test
    void testUpdateStatusFromShippedToInvalidStatusThrowsException() {
        UUID customerId = UUID.randomUUID();
        UUID productId = UUID.randomUUID();

        // Criação de um item de pedido real
        OrderItem item = new OrderItem(UUID.randomUUID(), productId, 2, 50.0);

        // Criação do pedido real
        Order order = new Order(
                OrderStatus.SHIPPED,
                customerId,
                List.of(item),
                new Address("Rua teste",
                        "123",
                        "Teste",
                        "Bairro teste",
                        "Cidade Teste",
                        "Estado Teste",
                        "Pais Teste",
                        "12345-678"),
                PaymentMethod.CREDIT_CARD,
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        when(orderRepositoryGateway.findById(order.getId())).thenReturn(order);

        // Teste para transição inválida de SHIPPED para qualquer status diferente de DELIVERED
        UUID orderId = order.getId();
        assertThrows(InvalidStatusException.class, () -> {
            updateOrderStatusUseCase.execute(orderId, OrderStatus.PROCESSING); // Transição inválida
        });

        verify(orderRepositoryGateway, never()).save(order);
    }

    @Test
    void testUpdateStatusWithInvalidStatusThrowsException() {
        UUID customerId = UUID.randomUUID();
        UUID productId = UUID.randomUUID();

        // Criação de um item de pedido real
        OrderItem item = new OrderItem(UUID.randomUUID(), productId, 2, 50.0);

        // Criação do pedido real
        Order order = new Order(
                OrderStatus.OPEN,
                customerId,
                List.of(item),
                new Address("Rua teste",
                        "123",
                        "Teste",
                        "Bairro teste",
                        "Cidade Teste",
                        "Estado Teste",
                        "Pais Teste",
                        "12345-678"),
                PaymentMethod.CREDIT_CARD,
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        when(orderRepositoryGateway.findById(order.getId())).thenReturn(order);

        // Teste para status inválido
        UUID orderId = order.getId();
        assertThrows(InvalidStatusException.class, () -> {
            updateOrderStatusUseCase.execute(orderId, null); // Status inválido
        });

        verify(orderRepositoryGateway, never()).save(order);
    }
}