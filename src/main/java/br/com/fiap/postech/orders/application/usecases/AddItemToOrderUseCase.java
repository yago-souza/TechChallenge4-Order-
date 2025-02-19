package br.com.fiap.postech.orders.application.usecases;

import br.com.fiap.postech.orders.domain.entities.Order;
import br.com.fiap.postech.orders.domain.entities.OrderItem;
import br.com.fiap.postech.orders.infrastructure.api.ProductGateway;
import br.com.fiap.postech.orders.infrastructure.exception.InsufficientStockException;
import br.com.fiap.postech.orders.infrastructure.exception.OrderNotFoundException;
import br.com.fiap.postech.orders.infrastructure.exception.ProductNotFoundException;
import br.com.fiap.postech.orders.infrastructure.gateway.impl.OrderRepositoryGatewayImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.UUID;

@Service
public class AddItemToOrderUseCase {
    //    Responsabilidade: Adicionar um novo item a um pedido existente.
    //    Lógica:
    //        Validar o produto e o estoque.
    //        Adicionar o item ao pedido.
    //        Recalcular o valor total do pedido.
    //        Salvar o pedido atualizado no banco de dados.

    private final OrderRepositoryGatewayImpl orderRepositoryGateway;
    private final ProductGateway productGateway;

    public AddItemToOrderUseCase(OrderRepositoryGatewayImpl orderRepositoryGateway, ProductGateway productGateway) {
        this.orderRepositoryGateway = orderRepositoryGateway;
        this.productGateway = productGateway;
    }

    @Transactional
    public Order execute(UUID orderId, OrderItem newItem) {
        // Busca o pedido existente
        Order order = orderRepositoryGateway.findById(orderId);
        if (order == null) {
            throw new OrderNotFoundException("Pedido não encontrado: " + orderId);
        }

        // Valida se o produto existe
        if (productGateway.getProductById(newItem.getProductId()) == null) {
            throw new ProductNotFoundException("Produto não encontrado: " + newItem.getProductId());
        }

        // Verifica o estoque disponível
        if (!productGateway.isInStock(newItem.getProductId(), newItem.getQuantity())) {
            throw new InsufficientStockException("Estoque insuficiente para o produto: " + newItem.getProductId());
        }

        if (newItem.getUnitPrice().compareTo(BigDecimal.ZERO) < 0) {
            throw new RuntimeException("Valor unitário invlaido para o produto: " + newItem.getProductId());
        }
        // Adiciona o item ao pedido
        order.addItem(newItem);

        // Atualiza o pedido no banco de dados
        return orderRepositoryGateway.save(order);
    }
}
