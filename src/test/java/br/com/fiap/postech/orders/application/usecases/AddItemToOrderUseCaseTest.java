package br.com.fiap.postech.orders.application.usecases;

import br.com.fiap.postech.orders.domain.entities.Order;
import br.com.fiap.postech.orders.domain.entities.OrderItem;
import br.com.fiap.postech.orders.infrastructure.api.ProductGateway;
import br.com.fiap.postech.orders.infrastructure.api.models.Product;
import br.com.fiap.postech.orders.infrastructure.exception.InsufficientStockException;
import br.com.fiap.postech.orders.infrastructure.exception.OrderNotFoundException;
import br.com.fiap.postech.orders.infrastructure.exception.ProductNotFoundException;
import br.com.fiap.postech.orders.infrastructure.gateway.impl.OrderRepositoryGatewayImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class AddItemToOrderUseCaseTest {

    @InjectMocks
    private AddItemToOrderUseCase addItemToOrderUseCase;

    @Mock
    private OrderRepositoryGatewayImpl orderRepositoryGateway;

    @Mock
    private ProductGateway productGateway;

    private Order order;
    private OrderItem orderItem;
    private UUID orderId;
    private UUID productId;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        orderId = UUID.randomUUID();
        productId = UUID.randomUUID();
        order = new Order();
        order.setId(orderId);
        orderItem = new OrderItem();
        orderItem.setProductId(productId);
        orderItem.setUnitPrice(BigDecimal.valueOf(10.01));
    }

    @Test
    void shouldAddItemToOrderSuccessfully() {
        Product product = new Product(productId, "Produto Teste", "Descrição", BigDecimal.valueOf(10.0), 100);

        when(orderRepositoryGateway.findById(orderId)).thenReturn(order);
        when(productGateway.getProductById(productId)).thenReturn(product); // Simula produto existente
        when(productGateway.isInStock(productId, orderItem.getQuantity())).thenReturn(true);
        when(orderRepositoryGateway.save(any(Order.class))).thenReturn(order);

        Order updatedOrder = addItemToOrderUseCase.execute(orderId, orderItem);

        assertNotNull(updatedOrder);
        assertFalse(updatedOrder.getItems().isEmpty());
        verify(orderRepositoryGateway, times(1)).save(order);
    }

    @Test
    void shouldThrowExceptionWhenOrderNotFound() {
        when(orderRepositoryGateway.findById(orderId)).thenReturn(null);

        assertThrows(OrderNotFoundException.class, () -> addItemToOrderUseCase.execute(orderId, orderItem));
    }

    @Test
    void shouldThrowExceptionWhenProductNotFound() {
        when(orderRepositoryGateway.findById(orderId)).thenReturn(order);
        when(productGateway.getProductById(productId)).thenReturn(null);

        assertThrows(ProductNotFoundException.class, () -> addItemToOrderUseCase.execute(orderId, orderItem));
    }

    @Test
    void shouldThrowExceptionWhenInsufficientStock() {
        Product product = new Product(productId, "Produto Teste", "Descrição", BigDecimal.valueOf(10.0), 100);

        when(orderRepositoryGateway.findById(orderId)).thenReturn(order);
        when(productGateway.getProductById(productId)).thenReturn(product);
        when(productGateway.isInStock(productId, orderItem.getQuantity())).thenReturn(false);

        assertThrows(InsufficientStockException.class, () -> addItemToOrderUseCase.execute(orderId, orderItem));
    }
}

