package br.com.fiap.postech.orders.infrastructure.gateway.impl;

import br.com.fiap.postech.orders.domain.entities.Order;
import br.com.fiap.postech.orders.domain.enums.OrderStatus;
import br.com.fiap.postech.orders.infrastructure.exception.OrderNotFoundException;
import br.com.fiap.postech.orders.infrastructure.gateway.OrderRepositoryGateway;
import br.com.fiap.postech.orders.infrastructure.mapper.OrderMapper;
import br.com.fiap.postech.orders.infrastructure.persistence.OrderEntity;
import br.com.fiap.postech.orders.infrastructure.persistence.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class OrderRepositoryGatewayImpl implements OrderRepositoryGateway {
    private final OrderRepository orderRepository;
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
        return orderMapper.toModel(entity);
    }

    @Override
    public boolean existsById(UUID id) {
        if (!orderRepository.existsById(id)) {
            throw new OrderNotFoundException("Pedido não encontrado");
        }
        return true;
    }

    @Override
    public void deleteById(UUID id) {
        orderRepository.deleteById(id);
    }

    @Override
    public Order save(Order order) {
        OrderEntity orderEntity = orderMapper.toEntity(order);
        OrderEntity entity = orderRepository.save(orderEntity);
        return orderMapper.toModel(entity);
    }

    public List<Order> findByCustomerIdAndStatus(UUID customerId, OrderStatus status) {
        List<OrderEntity> orderEntityList = orderRepository.findByCustomerIdAndStatus(customerId, status);
        return orderEntityList.stream()
                .map(orderMapper::toModel)
                .toList();
    }

    public List<Order> findByCustomerId(UUID customerId) {
        List<OrderEntity> orderEntityList = orderRepository.findByCustomerId(customerId);
        return orderEntityList.stream()
                .map(orderMapper::toModel)
                .toList();
    }

    public List<Order> findByStatus(OrderStatus orderStatus) {
        List<OrderEntity> orderEntityList = orderRepository.findByStatus(orderStatus);
        return orderEntityList.stream()
                .map(orderMapper::toModel)
                .toList();
    }
}
