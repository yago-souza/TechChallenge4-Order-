package br.com.fiap.postech.orders.application.usecases;

import br.com.fiap.postech.orders.domain.entities.Order;
import br.com.fiap.postech.orders.infrastructure.exception.OrderNotFoundException;
import br.com.fiap.postech.orders.infrastructure.gateway.impl.OrderRepositoryGatewayImpl;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class GetOrderUseCase {
    //    Responsabilidade: Buscar informações detalhadas de um pedido.
    //    Lógica:
    //        Buscar o pedido pelo ID.
    //        Retornar informações como status, itens, valor total, etc.
    private final OrderRepositoryGatewayImpl orderRepositoryGateway;

    public GetOrderUseCase(OrderRepositoryGatewayImpl orderRepositoryGateway) {
        this.orderRepositoryGateway = orderRepositoryGateway;
    }

    @Transactional
    public Order execute(UUID orderId) {
        Order order = orderRepositoryGateway.findById(orderId);
        if (order == null) {
            throw new OrderNotFoundException("Pedido não encontrado: " + orderId);
        }
        return orderRepositoryGateway.findById(orderId);
    }
}
