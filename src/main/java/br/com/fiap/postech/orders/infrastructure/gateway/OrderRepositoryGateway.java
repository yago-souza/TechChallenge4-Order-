package br.com.fiap.postech.orders.infrastructure.gateway;

import br.com.fiap.postech.orders.domain.entities.Order;

import java.util.List;
import java.util.UUID;

public interface OrderRepositoryGateway {
    List<Order> findAll();
    Order findById(UUID id);
    boolean existsById(UUID id);
    void deleteById(UUID id);
    Order save(Order order);
}
