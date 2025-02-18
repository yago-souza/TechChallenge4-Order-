package br.com.fiap.postech.orders.application.usecases;

import br.com.fiap.postech.orders.domain.entities.Order;
import br.com.fiap.postech.orders.domain.enums.OrderStatus;
import br.com.fiap.postech.orders.infrastructure.gateway.impl.OrderRepositoryGatewayImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ListOrdersUseCaseTest {
    @InjectMocks
    private ListOrdersUseCase  listOrdersUseCase;

    @Mock
    private OrderRepositoryGatewayImpl orderRepositoryGateway;

    private UUID customerId;
    private OrderStatus status;
    private Order mockOrder;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        customerId = UUID.randomUUID();
        status = OrderStatus.OPEN;
        mockOrder = new Order();
        mockOrder.setId(UUID.randomUUID());
        mockOrder.setCustomerId(customerId);
        mockOrder.setStatus(status);
    }

    @Test
    void shouldReturnOrdersWhenFilteringByCustomerIdAndStatus() {
        when(orderRepositoryGateway.findByCustomerIdAndStatus(customerId, status))
                .thenReturn(List.of(mockOrder));

        List<Order> result = listOrdersUseCase.execute(customerId, status);

        assertNotNull(result, "A lista de pedidos não deve ser nula");
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        assertEquals(customerId, result.get(0).getCustomerId());
        assertEquals(status, result.get(0).getStatus());

        verify(orderRepositoryGateway, times(1)).findByCustomerIdAndStatus(customerId, status);
        verifyNoMoreInteractions(orderRepositoryGateway);
    }

    @Test
    void shouldReturnOrdersWhenFilteringByCustomerIdOnly() {
        when(orderRepositoryGateway.findByCustomerId(customerId))
                .thenReturn(List.of(mockOrder));

        List<Order> result = listOrdersUseCase.execute(customerId, null);

        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());

        verify(orderRepositoryGateway, times(1)).findByCustomerId(customerId);
        verifyNoMoreInteractions(orderRepositoryGateway);
    }

    @Test
    void shouldReturnOrdersWhenFilteringByStatusOnly() {
        when(orderRepositoryGateway.findByStatus(status))
                .thenReturn(List.of(mockOrder));

        List<Order> result = listOrdersUseCase.execute(null, status);

        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());

        verify(orderRepositoryGateway, times(1)).findByStatus(status);
        verifyNoMoreInteractions(orderRepositoryGateway);
    }

    @Test
    void shouldReturnAllOrdersWhenNoFiltersAreProvided() {
        when(orderRepositoryGateway.findAll()).thenReturn(List.of(mockOrder));

        List<Order> result = listOrdersUseCase.execute(null, null);

        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());

        verify(orderRepositoryGateway, times(1)).findAll();
        verifyNoMoreInteractions(orderRepositoryGateway);
    }

    @Test
    void shouldReturnEmptyListWhenNoOrdersFound() {
        when(orderRepositoryGateway.findAll()).thenReturn(Collections.emptyList());

        List<Order> result = listOrdersUseCase.execute(null, null);

        assertNotNull(result);
        assertTrue(result.isEmpty());

        verify(orderRepositoryGateway, times(1)).findAll();
        verifyNoMoreInteractions(orderRepositoryGateway);
    }
}
