package br.com.fiap.postech.orders.service;

import br.com.fiap.postech.orders.domain.Order;
import br.com.fiap.postech.orders.domain.OrderItem;
import br.com.fiap.postech.orders.domain.OrderStatus;
import br.com.fiap.postech.orders.exeption.ClientNotFoundException;
import br.com.fiap.postech.orders.exeption.InsufficientStockException;
import br.com.fiap.postech.orders.exeption.ProductNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {

    @Mock
    private ClientService clientService;

    @Mock
    private ProductService productService;

    @InjectMocks
    private OrderService orderService;

    @Test
    public void testValidateOrder_ClientNotFound() {
        // Arrange
        UUID clientId = UUID.randomUUID();
        UUID productId = UUID.randomUUID();
        OrderItem item = new OrderItem(UUID.randomUUID(), productId, 2, 50.0, 100.0); // Usando o construtor correto
        Order order = new Order(
                UUID.randomUUID(), // id
                OrderStatus.OPEN, // status
                clientId, // clientId
                List.of(item), // items
                "Rua Exemplo, 123", // deliveryAddress
                100.0, // totalAmount
                "Cartão de Crédito", // paymentMethod
                LocalDateTime.now(), // estimatedDeliveryDate
                "ABC123", // trackingCode
                LocalDateTime.now(), // createdAt
                LocalDateTime.now() // updatedAt
        );

        when(clientService.existsById(clientId)).thenReturn(false);

        // Act & Assert
        assertThrows(ClientNotFoundException.class, () -> orderService.validateOrder(order));
    }

    @Test
    public void testValidateOrder_ProductNotFound() {
        // Arrange
        UUID clientId = UUID.randomUUID();
        UUID productId = UUID.randomUUID();
        OrderItem item = new OrderItem(UUID.randomUUID(), productId, 2, 50.0, 100.0); // Usando o construtor correto
        Order order = new Order(
                UUID.randomUUID(), // id
                OrderStatus.OPEN, // status
                clientId, // clientId
                List.of(item), // items
                "Rua Exemplo, 123", // deliveryAddress
                100.0, // totalAmount
                "Cartão de Crédito", // paymentMethod
                LocalDateTime.now(), // estimatedDeliveryDate
                "ABC123", // trackingCode
                LocalDateTime.now(), // createdAt
                LocalDateTime.now() // updatedAt
        );

        when(clientService.existsById(clientId)).thenReturn(true);
        when(productService.existsById(productId)).thenReturn(false);

        // Act & Assert
        assertThrows(ProductNotFoundException.class, () -> orderService.validateOrder(order));
    }

    @Test
    public void testValidateOrder_InsufficientStock() {
        // Arrange
        UUID clientId = UUID.randomUUID();
        UUID productId = UUID.randomUUID();
        OrderItem item = new OrderItem(UUID.randomUUID(), productId, 2, 50.0, 100.0); // Usando o construtor correto
        Order order = new Order(
                UUID.randomUUID(), // id
                OrderStatus.OPEN, // status
                clientId, // clientId
                List.of(item), // items
                "Rua Exemplo, 123", // deliveryAddress
                100.0, // totalAmount
                "Cartão de Crédito", // paymentMethod
                LocalDateTime.now(), // estimatedDeliveryDate
                "ABC123", // trackingCode
                LocalDateTime.now(), // createdAt
                LocalDateTime.now() // updatedAt
        );

        when(clientService.existsById(clientId)).thenReturn(true);
        when(productService.existsById(productId)).thenReturn(true);
        when(productService.isInStock(productId, 2)).thenReturn(false);

        // Act & Assert
        assertThrows(InsufficientStockException.class, () -> orderService.validateOrder(order));
    }

    @Test
    public void testValidateOrder_MultipleItems() {
        // Arrange
        UUID clientId = UUID.randomUUID();
        UUID productId1 = UUID.randomUUID();
        UUID productId2 = UUID.randomUUID();

        OrderItem item1 = new OrderItem(UUID.randomUUID(), productId1, 2, 50.0, 100.0);
        OrderItem item2 = new OrderItem(UUID.randomUUID(), productId2, 1, 100.0, 100.0);

        Order order = new Order(
                UUID.randomUUID(),
                OrderStatus.OPEN,
                clientId,
                List.of(item1, item2),
                "Rua Exemplo, 123",
                200.0,
                "Cartão de Crédito",
                LocalDateTime.now(),
                "ABC123",
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        when(clientService.existsById(clientId)).thenReturn(true);
        when(productService.existsById(productId1)).thenReturn(true);
        when(productService.existsById(productId2)).thenReturn(true);
        when(productService.isInStock(productId1, 2)).thenReturn(true);
        when(productService.isInStock(productId2, 1)).thenReturn(true);

        // Act & Assert
        assertDoesNotThrow(() -> orderService.validateOrder(order));
    }

    @Test
    public void testValidateOrder_InsufficientStockForMultipleItems() {
        // Arrange
        UUID clientId = UUID.randomUUID();
        UUID productId1 = UUID.randomUUID();
        UUID productId2 = UUID.randomUUID();

        OrderItem item1 = new OrderItem(UUID.randomUUID(), productId1, 2, 50.0, 100.0);
        OrderItem item2 = new OrderItem(UUID.randomUUID(), productId2, 1, 100.0, 100.0);

        Order order = new Order(
                UUID.randomUUID(),
                OrderStatus.OPEN,
                clientId,
                List.of(item1, item2),
                "Rua Exemplo, 123",
                200.0,
                "Cartão de Crédito",
                LocalDateTime.now(),
                "ABC123",
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        when(clientService.existsById(clientId)).thenReturn(true);
        when(productService.existsById(productId1)).thenReturn(true);
        when(productService.existsById(productId2)).thenReturn(true);
        when(productService.isInStock(productId1, 2)).thenReturn(true);
        when(productService.isInStock(productId2, 1)).thenReturn(false); // Estoque insuficiente para o segundo item

        // Act & Assert
        assertThrows(InsufficientStockException.class, () -> orderService.validateOrder(order));
    }

    @Test
    public void testUpdateStatus_ValidTransition() {
        // Arrange
        Order order = new Order(
                UUID.randomUUID(),
                OrderStatus.OPEN,
                UUID.randomUUID(),
                List.of(new OrderItem(UUID.randomUUID(), UUID.randomUUID(), 2, 50.0, 100.0)),
                "Rua Exemplo, 123",
                100.0,
                "Cartão de Crédito",
                LocalDateTime.now(),
                "ABC123",
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        // Act
        order.updateStatus(OrderStatus.PAID);

        // Assert
        assertEquals(OrderStatus.PAID, order.getStatus());
    }

    @Test
    public void testUpdateStatus_InvalidTransition() {
        // Arrange
        Order order = new Order(
                UUID.randomUUID(),
                OrderStatus.DELIVERED,
                UUID.randomUUID(),
                List.of(new OrderItem(UUID.randomUUID(), UUID.randomUUID(), 2, 50.0, 100.0)),
                "Rua Exemplo, 123",
                100.0,
                "Cartão de Crédito",
                LocalDateTime.now(),
                "ABC123",
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        // Act & Assert
        assertThrows(IllegalStateException.class, () -> order.updateStatus(OrderStatus.OPEN));
    }

    @Test
    public void testCalculateTotalAmount() {
        // Arrange
        Order order = new Order(
                UUID.randomUUID(),
                OrderStatus.OPEN,
                UUID.randomUUID(),
                List.of(),
                "Rua Exemplo, 123",
                0.0,
                "Cartão de Crédito",
                LocalDateTime.now(),
                "ABC123",
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        OrderItem item1 = new OrderItem(UUID.randomUUID(), UUID.randomUUID(), 2, 50.0, 100.0);
        OrderItem item2 = new OrderItem(UUID.randomUUID(), UUID.randomUUID(), 1, 100.0, 100.0);

        // Act
        order.addItem(item1);
        order.addItem(item2);

        // Assert
        assertEquals(200.0, order.getTotalAmount());
    }
}