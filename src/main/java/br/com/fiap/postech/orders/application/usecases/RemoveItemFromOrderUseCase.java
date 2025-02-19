package br.com.fiap.postech.orders.application.usecases;

import br.com.fiap.postech.orders.domain.entities.Order;
import br.com.fiap.postech.orders.domain.entities.OrderItem;
import br.com.fiap.postech.orders.infrastructure.exception.OrderNotFoundException;
import br.com.fiap.postech.orders.infrastructure.exception.ProductNotFoundException;
import br.com.fiap.postech.orders.infrastructure.gateway.impl.OrderRepositoryGatewayImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class RemoveItemFromOrderUseCase {
    //    Responsabilidade: Remover um item de um pedido existente.
    //
    //    Lógica:
    //
    //        Remover o item do pedido.
    //
    //        Recalcular o valor total do pedido.
    //
    //        Salvar o pedido atualizado no banco de dados.
    private final OrderRepositoryGatewayImpl orderRepositoryGateway;

    public RemoveItemFromOrderUseCase(OrderRepositoryGatewayImpl orderRepositoryGateway) {
        this.orderRepositoryGateway = orderRepositoryGateway;
    }

    @Transactional
    public Order execute(UUID orderId, UUID productId) {
        // Buscar o pedido
        Order order = orderRepositoryGateway.findById(orderId);
        if (order == null) {
            throw new OrderNotFoundException("Pedido não encontrado: " + orderId);
        }

        // Encontrar o item no pedido
        OrderItem itemToRemove = order.getItems().stream()
                .filter(item -> item.getProductId().equals(productId))
                .findFirst()
                .orElseThrow(() -> new ProductNotFoundException("Produto não encontrado no pedido: " + productId));

        // Remover o item
        order.removeItem(itemToRemove);

        // Salvar o pedido atualizado
        return orderRepositoryGateway.save(order);
    }
}
