package br.com.fiap.postech.orders.infrastructure.persistence;

import br.com.fiap.postech.orders.domain.enums.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface OrderRepository extends JpaRepository<OrderEntity, UUID> {
    List<OrderEntity> findByCustomerIdAndStatus(UUID customerId, OrderStatus status);
    List<OrderEntity> findByCustomerId(UUID customerId);
    List<OrderEntity> findByStatus(OrderStatus status);
}