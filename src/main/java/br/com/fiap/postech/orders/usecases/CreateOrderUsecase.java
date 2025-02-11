package br.com.fiap.postech.orders.usecases;

import br.com.fiap.postech.orders.domain.enums.OrderStatus;
import br.com.fiap.postech.orders.domain.models.Product;
import br.com.fiap.postech.orders.domain.entities.Order;
import br.com.fiap.postech.orders.domain.gateways.ClientGateway;
import br.com.fiap.postech.orders.domain.gateways.ProductGateway;
import br.com.fiap.postech.orders.infrastructure.exception.ClientNotFoundException;
import br.com.fiap.postech.orders.infrastructure.exception.InsufficientStockException;
import br.com.fiap.postech.orders.infrastructure.exception.ProductNotFoundException;
import br.com.fiap.postech.orders.infrastructure.persistence.repositories.OrderRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class CreateOrderUsecase {

    private final OrderRepository orderRepository;
    private final ClientGateway clientGateway;
    private final ProductGateway productGateway;

    public CreateOrderUsecase(OrderRepository orderRepository, ClientGateway clientGateway, ProductGateway productGateway) {
        this.orderRepository = orderRepository;
        this.clientGateway = clientGateway;
        this.productGateway = productGateway;
    }

    @Transactional
    public Order execute(Order order) {
        // Validar se o cliente existe
        if (clientGateway.getClientById(order.getClientId()) == null) {
            throw new ClientNotFoundException("Cliente não encontrado: " + order.getClientId());
        }

        // Validar produtos e estoque
        order.getItems().forEach(item -> {
            Product product = productGateway.getProductById(item.getProductId());
            if (product == null) {
                throw new ProductNotFoundException("Produto não encontrado: " + item.getProductId());
            }
            if (!productGateway.isInStock(item.getProductId(), item.getQuantity())) {
                throw new InsufficientStockException("Estoque insuficiente para o produto: " + item.getProductId());
            }
        });

        order.setStatus(OrderStatus.OPEN);
        order.setCreatedAt(LocalDateTime.now());
        order.setUpdatedAt(LocalDateTime.now());

        return orderRepository.save(order);
    }
}
