package br.com.fiap.postech.orders.application.usecases;

import br.com.fiap.postech.orders.domain.entities.Order;
import br.com.fiap.postech.orders.domain.enums.OrderStatus;
import br.com.fiap.postech.orders.infrastructure.exception.InvalidStatusException;
import br.com.fiap.postech.orders.infrastructure.exception.OrderNotFoundException;
import br.com.fiap.postech.orders.infrastructure.persistence.OrderRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.UUID;

@Service
public class UpdateOrderStatusUseCase {
    private final OrderRepository orderRepository;

    public UpdateOrderStatusUseCase(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Transactional
    public Order execute(UUID orderId, OrderStatus newOrderStatus) {
        // Valida se o pedido já existe
        Order currentOrder = validateOrder(orderId);

        // Valida se o novo status é diferente do atual
        validateStatusTransition(currentOrder.getStatus(), newOrderStatus);

        // Atualiza status
        currentOrder.setStatus(newOrderStatus);

        // Presiste o pedido
        return orderRepository.save(currentOrder);
    }

    private Order validateOrder (UUID orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException("Pedido " + orderId + " não encontrado"));
    }

    private void validateStatusTransition(OrderStatus currentStatus, OrderStatus newStatus) {
        // 1. Verifica se o novo status é o mesmo do atual
        if (currentStatus.equals(newStatus)) {
            throw new InvalidStatusException("O novo status é o mesmo do antigo.");
        }

        // 2. Verifica se o pedido está em um status final
        if (currentStatus.equals(OrderStatus.DELIVERED) ||
                currentStatus.equals(OrderStatus.CANCELED) ||
                currentStatus.equals(OrderStatus.RETURNED)) {
            throw new InvalidStatusException("Pedidos em status final não podem ser alterados.");
        }

        // 3. Verifica transições inválidas
        if (currentStatus.equals(OrderStatus.OPEN) && !newStatus.equals(OrderStatus.PAID) && !newStatus.equals(OrderStatus.CANCELED)) {
            throw new InvalidStatusException("Pedidos abertos só podem ser marcados como pagos ou cancelados.");
        }

        if (currentStatus.equals(OrderStatus.PAID) && !newStatus.equals(OrderStatus.PROCESSING) && !newStatus.equals(OrderStatus.CANCELED)) {
            throw new InvalidStatusException("Pedidos pagos só podem ser processados ou cancelados.");
        }

        if (currentStatus.equals(OrderStatus.PROCESSING) && !newStatus.equals(OrderStatus.SHIPPED) && !newStatus.equals(OrderStatus.CANCELED)) {
            throw new InvalidStatusException("Pedidos em processamento só podem ser enviados ou cancelados.");
        }

        if (currentStatus.equals(OrderStatus.SHIPPED) && !newStatus.equals(OrderStatus.DELIVERED)) {
            throw new InvalidStatusException("Pedidos enviados só podem ser marcados como entregues.");
        }

        if (currentStatus.equals(OrderStatus.DELIVERED) && !newStatus.equals(OrderStatus.RETURNED)) {
            throw new InvalidStatusException("Pedidos entregues só podem ser marcados como devolvidos.");
        }

        if (currentStatus.equals(OrderStatus.CANCELED) && !newStatus.equals(OrderStatus.OPEN)) {
            throw new InvalidStatusException("Pedidos cancelados só podem ser reabertos.");
        }

        if (currentStatus.equals(OrderStatus.RETURNED)) {
            throw new InvalidStatusException("Pedidos devolvidos não podem ser alterados.");
        }

        // 4. Verifica se o novo status é válido
        if (!Arrays.asList(OrderStatus.values()).contains(newStatus)) {
            throw new InvalidStatusException("Status inválido: " + newStatus);
        }
    }
}
