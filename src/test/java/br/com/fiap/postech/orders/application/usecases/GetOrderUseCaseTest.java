package br.com.fiap.postech.orders.application.usecases;

import br.com.fiap.postech.orders.domain.entities.Order;
import br.com.fiap.postech.orders.infrastructure.exception.OrderNotFoundException;
import br.com.fiap.postech.orders.infrastructure.gateway.impl.OrderRepositoryGatewayImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GetOrderUseCaseTest {

    @InjectMocks
    private GetOrderUseCase getOrderUseCase;

    @Mock
    private OrderRepositoryGatewayImpl orderRepositoryGateway;

    private Order order;
    private UUID orderId;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        orderId = UUID.randomUUID();
        order = new Order();
        order.setId(orderId);
    }

    @Test
    void shouldReturnOrderDetailsSuccessfully() {
        when(orderRepositoryGateway.findById(orderId)).thenReturn(order);

        Order trackedOrder = getOrderUseCase.execute(orderId);

        assertNotNull(trackedOrder);
        assertEquals(orderId, trackedOrder.getId());
    }

    @Test
    void shouldThrowExceptionWhenOrderNotFound() {
        when(orderRepositoryGateway.findById(orderId)).thenReturn(null);

        assertThrows(OrderNotFoundException.class, () -> getOrderUseCase.execute(orderId));
    }
}