package br.com.fiap.postech.orders.usecases;

import br.com.fiap.postech.orders.domain.entities.Order;
import br.com.fiap.postech.orders.domain.entities.OrderItem;
import br.com.fiap.postech.orders.domain.enums.OrderStatus;
import br.com.fiap.postech.orders.domain.enums.PaymentMethod;
import br.com.fiap.postech.orders.domain.gateways.CustomerGateway;
import br.com.fiap.postech.orders.domain.gateways.ProductGateway;
import br.com.fiap.postech.orders.domain.models.Customer;
import br.com.fiap.postech.orders.domain.models.Product;
import br.com.fiap.postech.orders.infrastructure.persistence.repositories.OrderRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.util.AssertionErrors.assertEquals;

//@ExtendWith(MockitoExtension.class)
//public class CreateOrderUsecaseTest {
//
//    @Mock
//    private OrderRepository orderRepository;
//
//    @Mock
//    private CustomerGateway customerGateway;
//
//    @Mock
//    private ProductGateway productGateway;
//
//    @InjectMocks
//    private CreateOrderUseCase createOrderUseCase;
//
//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.openMocks(this);
//    }
//
//    @Test
//    void deveCriarPedidoComSucesso() {
//        // Arrange
//        UUID customerId = UUID.randomUUID();
//        UUID productId = UUID.randomUUID();
//        UUID orderId = UUID.randomUUID();
//        Order order = new Order();
//        order.setOrderId(orderId);
//
//        OrderItem item = new OrderItem(UUID.randomUUID(), order, productId, 2, 50.0, 100.0, LocalDateTime.now(), LocalDateTime.now());
//
//        order.setStatus(OrderStatus.OPEN);
//        order.setCustomerId(customerId);
//        order.setItems(List.of(item));
//        order.setDeliveryAddress("Rua Exemplo, 123");
//        order.setPaymentMethod(PaymentMethod.CREDIT_CARD);
//        order.setEstimatedDeliveryDate(LocalDateTime.now());
//        order.setTrackingCode("ABC123");
//        order.setCreatedAt(LocalDateTime.now());
//
//        when(customerGateway.getCustomerById(customerId)).thenReturn(new Customer(customerId, "Cliente Teste", "teste@teste.com", "Rua teste"));
//        when(productGateway.getProductById(productId)).thenReturn(new Product(productId, "Produto Teste", "teste", 10.01, 30));
//        when(productGateway.isInStock(productId, 2)).thenReturn(true);
//        when(orderRepository.save(any(Order.class))).thenAnswer(invocation -> invocation.getArgument(0));
//
//        // Act
//        Order createdOrder = createOrderUseCase.execute(order);
//
//        // Assert
//        assertNotNull(createdOrder);
//        //assertEquals(OrderStatus.OPEN, createdOrder.getStatus());
//        Assertions.assertEquals(customerId, createdOrder.getCustomerId());
//        Assertions.assertEquals(1, createdOrder.getItems().size());
//        Assertions.assertEquals(productId, createdOrder.getItems().get(0).getProductId());
//        verify(orderRepository, times(1)).save(order);
//    }
//}
//
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