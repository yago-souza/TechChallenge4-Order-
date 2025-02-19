package br.com.fiap.postech.orders.application.usecases;

import br.com.fiap.postech.orders.domain.entities.Order;
import br.com.fiap.postech.orders.domain.entities.OrderItem;
import br.com.fiap.postech.orders.domain.enums.OrderStatus;
import br.com.fiap.postech.orders.infrastructure.exception.OrderNotFoundException;
import br.com.fiap.postech.orders.infrastructure.exception.ProductNotFoundException;
import br.com.fiap.postech.orders.infrastructure.gateway.impl.OrderRepositoryGatewayImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class RemoveItemFromOrderUseCaseTest {

    @InjectMocks
    private RemoveItemFromOrderUseCase removeItemFromOrderUseCase;

    @Mock
    private OrderRepositoryGatewayImpl orderRepositoryGateway;

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
        order.setStatus(OrderStatus.OPEN);
        orderItem.setProductId(productId);
        order.addItem(orderItem);
    }

    @Test
    void shouldRemoveItemFromOrderSuccessfully() {
        when(orderRepositoryGateway.findById(orderId)).thenReturn(order);
        when(orderRepositoryGateway.save(any(Order.class))).thenReturn(order);

        Order updatedOrder = removeItemFromOrderUseCase.execute(orderId, productId);

        assertNotNull(updatedOrder);
        assertTrue(updatedOrder.getItems().isEmpty());
        verify(orderRepositoryGateway, times(1)).save(order);
    }

    @Test
    void shouldThrowExceptionWhenOrderNotFound() {
        when(orderRepositoryGateway.findById(orderId)).thenReturn(null);

        assertThrows(OrderNotFoundException.class, () -> removeItemFromOrderUseCase.execute(orderId, productId));
    }

    @Test
    void shouldThrowExceptionWhenProductNotFoundInOrder() {
        order.clearItems();
        when(orderRepositoryGateway.findById(orderId)).thenReturn(order);

        assertThrows(ProductNotFoundException.class, () -> removeItemFromOrderUseCase.execute(orderId, productId));
    }
}
