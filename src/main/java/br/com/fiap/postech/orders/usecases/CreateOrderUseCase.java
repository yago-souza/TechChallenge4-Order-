package br.com.fiap.postech.orders.usecases;

import br.com.fiap.postech.orders.domain.entities.Order;

public interface CreateOrderUseCase {
    Order createOrder(Order order);
}
