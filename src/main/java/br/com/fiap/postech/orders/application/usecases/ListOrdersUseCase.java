package br.com.fiap.postech.orders.application.usecases;

import br.com.fiap.postech.orders.domain.entities.Order;
import br.com.fiap.postech.orders.domain.enums.OrderStatus;
import br.com.fiap.postech.orders.infrastructure.gateway.impl.OrderRepositoryGatewayImpl;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ListOrdersUseCase {
    //    Responsabilidade: Listar todos os pedidos ou filtrar por status, cliente, etc.
    //    Lógica:
    //        Buscar pedidos no banco de dados com base em critérios de filtro.
    //        Retornar uma lista de pedidos.

        private final OrderRepositoryGatewayImpl orderRepositoryGateway;

        public ListOrdersUseCase(OrderRepositoryGatewayImpl orderRepositoryGateway) {
            this.orderRepositoryGateway = orderRepositoryGateway;
        }

        @Transactional
        public List<Order> execute(UUID customerId, OrderStatus status) {
            if (customerId != null && status != null) {
                return orderRepositoryGateway.findByCustomerIdAndStatus(customerId, status);
            } else if (customerId != null) {
                return orderRepositoryGateway.findByCustomerId(customerId);
            } else if (status != null) {
                return orderRepositoryGateway.findByStatus(status);
            } else {
                return orderRepositoryGateway.findAll();
            }
        }
}
