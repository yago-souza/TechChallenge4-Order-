package br.com.fiap.postech.orders.infrastructure.persistence.repositories;

import br.com.fiap.postech.orders.domain.entities.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface OrderRepository extends JpaRepository<Order, UUID> {
}