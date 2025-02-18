package br.com.fiap.postech.orders.application.usecases;

import br.com.fiap.postech.orders.domain.entities.Order;
import br.com.fiap.postech.orders.domain.enums.OrderStatus;
import br.com.fiap.postech.orders.infrastructure.exception.InvalidStatusException;
import br.com.fiap.postech.orders.infrastructure.gateway.impl.OrderRepositoryGatewayImpl;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UpdateOrderStatusUseCase {
    private final OrderRepositoryGatewayImpl orderRepositoryGateway;

    public UpdateOrderStatusUseCase(OrderRepositoryGatewayImpl orderRepository) {
        this.orderRepositoryGateway = orderRepository;
    }

    @Transactional
    public Order execute(UUID orderId, OrderStatus newOrderStatus) {
        // Valida se o pedido já existe
        Order currentOrder = orderRepositoryGateway.findById(orderId);

        // Valida se o novo status é diferente do atual
        validateStatusTransition(currentOrder.getStatus(), newOrderStatus);

        // Atualiza status
        currentOrder.setStatus(newOrderStatus);

        // Presiste o pedido
        return orderRepositoryGateway.save(currentOrder);
    }

    private void validateStatusTransition(OrderStatus currentStatus, OrderStatus newStatus) {
        // 1. Verifica se o novo status é o mesmo do atual
        if (currentStatus.equals(newStatus)) {
            throw new InvalidStatusException("O novo status é o mesmo do antigo.");
        }

        // 2. Verifica se o pedido está em um status final
        if (currentStatus == OrderStatus.DELIVERED ||
                currentStatus == OrderStatus.CANCELED ||
                currentStatus == OrderStatus.RETURNED) {
            throw new InvalidStatusException("Pedidos em status final não podem ser alterados.");
        }

        // 3. Verifica transições inválidas
        if (currentStatus == OrderStatus.OPEN && newStatus != OrderStatus.PAID && newStatus != OrderStatus.CANCELED) {
            throw new InvalidStatusException("Pedidos abertos só podem ser marcados como pagos ou cancelados.");
        }

        if (currentStatus == OrderStatus.PAID && newStatus != OrderStatus.PROCESSING && newStatus != OrderStatus.CANCELED) {
            throw new InvalidStatusException("Pedidos pagos só podem ser processados ou cancelados.");
        }

        if (currentStatus == OrderStatus.PROCESSING && newStatus != OrderStatus.SHIPPED && newStatus != OrderStatus.CANCELED) {
            throw new InvalidStatusException("Pedidos em processamento só podem ser enviados ou cancelados.");
        }

        if (currentStatus == OrderStatus.SHIPPED && newStatus != OrderStatus.DELIVERED) {
            throw new InvalidStatusException("Pedidos enviados só podem ser marcados como entregues.");
        }
    }
}
