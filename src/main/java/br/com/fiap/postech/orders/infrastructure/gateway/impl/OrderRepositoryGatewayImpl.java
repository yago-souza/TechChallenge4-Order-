package br.com.fiap.postech.orders.infrastructure.gateway.impl;

import br.com.fiap.postech.orders.domain.entities.Order;
import br.com.fiap.postech.orders.infrastructure.exception.OrderNotFoundException;
import br.com.fiap.postech.orders.infrastructure.gateway.OrderRepositoryGateway;
import br.com.fiap.postech.orders.infrastructure.mapper.OrderMapper;
import br.com.fiap.postech.orders.infrastructure.persistence.OrderEntity;
import br.com.fiap.postech.orders.infrastructure.persistence.OrderItemRepository;
import br.com.fiap.postech.orders.infrastructure.persistence.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class OrderRepositoryGatewayImpl implements OrderRepositoryGateway {
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final OrderMapper orderMapper;

    @Override
    public List<Order> findAll() {
        return List.of();
    }

    @Override
    public Order findById(UUID id) {
        OrderEntity entity = orderRepository
                .findById(id)
                .orElseThrow(() -> new OrderNotFoundException("Pedido não encontrado"));
        return null;
    }

    @Override
    public boolean existsById(UUID id) {
        if (!orderItemRepository.existsById(id)) {
            throw new OrderNotFoundException("Pedido não encontrado");
        }
        return true;
    }

    @Override
    public void deleteById(UUID id) {
        orderItemRepository.deleteById(id);
    }

    @Override
    public Order save(Order order) {
        OrderEntity orderEntity = orderMapper.toEntity(order);
        OrderEntity entity = orderRepository.save(orderEntity);
        return orderMapper.toModel(entity);
    }
}
