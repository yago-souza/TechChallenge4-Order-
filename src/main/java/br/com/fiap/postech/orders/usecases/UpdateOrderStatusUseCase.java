package br.com.fiap.postech.orders.usecases;

import br.com.fiap.postech.orders.domain.entities.Order;
import br.com.fiap.postech.orders.domain.enums.OrderStatus;

import java.util.UUID;

public interface UpdateOrderStatusUseCase {
    Order updateOrderStatus(UUID orderId, OrderStatus newStatus);
}
