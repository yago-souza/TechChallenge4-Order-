package br.com.fiap.postech.orders.usecases;

import br.com.fiap.postech.orders.domain.entities.Order;
import br.com.fiap.postech.orders.domain.entities.OrderItem;
import br.com.fiap.postech.orders.domain.enums.OrderStatus;
import br.com.fiap.postech.orders.domain.enums.PaymentMethod;
import br.com.fiap.postech.orders.infrastructure.exception.ClientNotFoundException;
import br.com.fiap.postech.orders.infrastructure.exception.InsufficientStockException;
import br.com.fiap.postech.orders.infrastructure.exception.ProductNotFoundException;
import br.com.fiap.postech.orders.services.ClientService;
import br.com.fiap.postech.orders.services.ProductService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

//@ExtendWith(MockitoExtension.class)
//public class CreateOrderUsecaseTest {
//
//    @Mock
//    private ClientService clientService;
//
//    @Mock
//    private ProductService productService;
//
//    @InjectMocks
//    private CreateOrderUsecase createOrderUsecase;
//
//    @Test
//    public void testCreateOrder_ClientNotFound() {
//        UUID clientId = UUID.randomUUID();
//        UUID productId = UUID.randomUUID();
//        UUID orderId = UUID.randomUUID();
//        Order order = new Order();
//        order.setOrderId(orderId);
//
//        OrderItem item = new OrderItem(UUID.randomUUID(), order, productId, 2, 50.0, 100.0, LocalDateTime.now(), LocalDateTime.now());
//
//        order.setStatus(OrderStatus.OPEN);
//        order.setClientId(clientId);
//        order.setItems(List.of(item));
//        order.setDeliveryAddress("Rua Exemplo, 123");
//        order.setPaymentMethod(PaymentMethod.CREDIT_CARD);
//        order.setEstimatedDeliveryDate(LocalDateTime.now());
//        order.setTrackingCode("ABC123");
//        order.setCreatedAt(LocalDateTime.now());
//
//        when(clientService.existsById(clientId)).thenReturn(false);
//
//        assertThrows(ClientNotFoundException.class, () -> createOrderUsecase.execute(order));
//    }
//
//    @Test
//    public void testCreateOrder_ProductNotFound() {
//        UUID clientId = UUID.randomUUID();
//        UUID productId = UUID.randomUUID();
//        UUID orderId = UUID.randomUUID();
//        Order order = new Order();
//        order.setOrderId(orderId);
//
//        OrderItem item = new OrderItem(UUID.randomUUID(), order, productId, 2, 50.0, 100.0, LocalDateTime.now(), LocalDateTime.now());
//
//        order.setStatus(OrderStatus.OPEN);
//        order.setClientId(clientId);
//        order.setItems(List.of(item));
//        order.setDeliveryAddress("Rua Exemplo, 123");
//        order.setPaymentMethod(PaymentMethod.CREDIT_CARD);
//        order.setEstimatedDeliveryDate(LocalDateTime.now());
//        order.setTrackingCode("ABC123");
//        order.setCreatedAt(LocalDateTime.now());
//
//        when(clientService.existsById(clientId)).thenReturn(true);
//        when(productService.existsById(productId)).thenReturn(false);
//
//        assertThrows(ProductNotFoundException.class, () -> createOrderUsecase.execute(order));
//    }
//
//    @Test
//    public void testCreateOrder_InsufficientStock() {
//        UUID clientId = UUID.randomUUID();
//        UUID productId = UUID.randomUUID();
//        UUID orderId = UUID.randomUUID();
//        Order order = new Order();
//        order.setOrderId(orderId);
//
//        OrderItem item = new OrderItem(UUID.randomUUID(), order, productId, 2, 50.0, 100.0, LocalDateTime.now(), LocalDateTime.now());
//
//        order.setStatus(OrderStatus.OPEN);
//        order.setClientId(clientId);
//        order.setItems(List.of(item));
//        order.setDeliveryAddress("Rua Exemplo, 123");
//        order.setPaymentMethod(PaymentMethod.CREDIT_CARD);
//        order.setEstimatedDeliveryDate(LocalDateTime.now());
//        order.setTrackingCode("ABC123");
//        order.setCreatedAt(LocalDateTime.now());
//
//        when(clientService.existsById(clientId)).thenReturn(true);
//        when(productService.existsById(productId)).thenReturn(true);
//        when(productService.isInStock(productId, 2)).thenReturn(false);
//
//        assertThrows(InsufficientStockException.class, () -> createOrderUsecase.execute(order));
//    }
//
//    @Test
//    public void testCreateOrder_Success() {
//        UUID clientId = UUID.randomUUID();
//        UUID productId = UUID.randomUUID();
//        UUID orderId = UUID.randomUUID();
//        Order order = new Order();
//        order.setOrderId(orderId);
//
//        OrderItem item = new OrderItem(UUID.randomUUID(), order, productId, 2, 50.0, 100.0, LocalDateTime.now(), LocalDateTime.now());
//
//        order.setStatus(OrderStatus.OPEN);
//        order.setClientId(clientId);
//        order.setItems(List.of(item));
//        order.setDeliveryAddress("Rua Exemplo, 123");
//        order.setPaymentMethod(PaymentMethod.CREDIT_CARD);
//        order.setEstimatedDeliveryDate(LocalDateTime.now());
//        order.setTrackingCode("ABC123");
//        order.setCreatedAt(LocalDateTime.now());
//
//        when(clientService.existsById(clientId)).thenReturn(true);
//        when(productService.existsById(productId)).thenReturn(true);
//        when(productService.isInStock(productId, 2)).thenReturn(true);
//
//        assertDoesNotThrow(() -> createOrderUsecase.execute(order));
//    }
//}