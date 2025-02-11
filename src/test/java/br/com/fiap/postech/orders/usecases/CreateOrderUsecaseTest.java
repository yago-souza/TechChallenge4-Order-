package br.com.fiap.postech.orders.usecases;

import br.com.fiap.postech.orders.domain.entities.Order;
import br.com.fiap.postech.orders.domain.entities.OrderItem;
import br.com.fiap.postech.orders.domain.enums.OrderStatus;
import br.com.fiap.postech.orders.domain.gateways.ClientGateway;
import br.com.fiap.postech.orders.domain.gateways.ProductGateway;
import br.com.fiap.postech.orders.domain.models.Client;
import br.com.fiap.postech.orders.domain.models.Product;
import br.com.fiap.postech.orders.infrastructure.exception.ClientNotFoundException;
import br.com.fiap.postech.orders.infrastructure.persistence.repositories.OrderRepository;
import br.com.fiap.postech.orders.services.ClientService;
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

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

public class CreateOrderUsecaseTest {

    @InjectMocks
    private CreateOrderUsecase createOrderUsecase;

    @Mock
    private ClientGateway clientGateway;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private ProductGateway productGateway;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldCreateOrderSuccessfully() {
        UUID clientId = UUID.randomUUID();
        UUID productId = UUID.randomUUID();

        OrderItem item = new OrderItem(UUID.randomUUID(), productId, 2, 50.0, 100.0);

        Order order = new Order(
                UUID.randomUUID(),
                OrderStatus.OPEN,
                clientId,
                List.of(item),
                "Rua Exemplo, 123",
                100.0,
                "Cartão de Crédito",
                LocalDateTime.now(),
                "ABC123",
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        Product product = new Product(
                productId,
                "teste",
                "teste",
                10.0,
                100
        );


        when(clientGateway.getClientById(order.getClientId())).thenReturn(new Client(clientId, "Test Client", "test@test.com", "teste, 123, teste"));
        when(productGateway.getProductById(any())).thenReturn(product);
        when(productGateway.isInStock(any(), any(Integer.class))).thenReturn(true);
        when(orderRepository.save(order)).thenReturn(order);

        when(orderRepository.save(order)).thenReturn(order);
        assertDoesNotThrow(() -> createOrderUsecase.execute(order));
    }
//
//    @Test
//    public void testCreateOrder_ClientNotFound() {
//        UUID clientId = UUID.randomUUID();
//        UUID productId = UUID.randomUUID();
//        OrderItem item = new OrderItem(UUID.randomUUID(), productId, 2, 50.0, 100.0);
//        Order order = new Order(
//                UUID.randomUUID(),
//                OrderStatus.OPEN,
//                clientId,
//                List.of(item),
//                "Rua Exemplo, 123",
//                100.0,
//                "Cartão de Crédito",
//                LocalDateTime.now(),
//                "ABC123",
//                LocalDateTime.now(),
//                LocalDateTime.now()
//        );
//
//        when(clientService.existsById(clientId)).thenReturn(false);
//
//        assertThrows(ClientNotFoundException.class, () -> createOrderUsecase.execute(order));
//    }

//    @Test
//    public void testCreateOrder_ProductNotFound() {
//        UUID clientId = UUID.randomUUID();
//        UUID productId = UUID.randomUUID();
//        OrderItem item = new OrderItem(UUID.randomUUID(), productId, 2, 50.0, 100.0);
//        Order order = new Order(
//                UUID.randomUUID(),
//                OrderStatus.OPEN,
//                clientId,
//                List.of(item),
//                "Rua Exemplo, 123",
//                100.0,
//                "Cartão de Crédito",
//                LocalDateTime.now(),
//                "ABC123",
//                LocalDateTime.now(),
//                LocalDateTime.now()
//        );
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
//        OrderItem item = new OrderItem(UUID.randomUUID(), productId, 2, 50.0, 100.0);
//        Order order = new Order(
//                UUID.randomUUID(),
//                OrderStatus.OPEN,
//                clientId,
//                List.of(item),
//                "Rua Exemplo, 123",
//                100.0,
//                "Cartão de Crédito",
//                LocalDateTime.now(),
//                "ABC123",
//                LocalDateTime.now(),
//                LocalDateTime.now()
//        );
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
//        OrderItem item = new OrderItem(UUID.randomUUID(), productId, 2, 50.0, 100.0);
//        Order order = new Order(
//                UUID.randomUUID(),
//                OrderStatus.OPEN,
//                clientId,
//                List.of(item),
//                "Rua Exemplo, 123",
//                100.0,
//                "Cartão de Crédito",
//                LocalDateTime.now(),
//                "ABC123",
//                LocalDateTime.now(),
//                LocalDateTime.now()
//        );
//
//        when(clientService.existsById(clientId)).thenReturn(true);
//        when(productService.existsById(productId)).thenReturn(true);
//        when(productService.isInStock(productId, 2)).thenReturn(true);
//
//        assertDoesNotThrow(() -> createOrderUsecase.execute(order));
//    }
}