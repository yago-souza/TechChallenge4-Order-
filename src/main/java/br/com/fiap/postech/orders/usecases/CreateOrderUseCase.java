package br.com.fiap.postech.orders.usecases;

import br.com.fiap.postech.orders.domain.entities.Order;
import br.com.fiap.postech.orders.domain.enums.OrderStatus;
import br.com.fiap.postech.orders.infrastructure.exception.InsufficientStockException;
import br.com.fiap.postech.orders.infrastructure.http.RestTemplateCustomerGateway;
import br.com.fiap.postech.orders.infrastructure.http.RestTemplateProductGateway;
import br.com.fiap.postech.orders.infrastructure.persistence.repositories.OrderRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class CreateOrderUseCase {

    private final OrderRepository orderRepository;
    private final RestTemplateCustomerGateway customerGateway;
    private final RestTemplateProductGateway productGateway;

    public CreateOrderUseCase(OrderRepository orderRepository, RestTemplateCustomerGateway customerGateway, RestTemplateProductGateway productGateway) {
        this.orderRepository = orderRepository;
        this.customerGateway = customerGateway;
        this.productGateway = productGateway;
    }

    @Transactional
    public Order execute(Order order) {
        // Validação do cliente

        customerGateway.getCustomerById(order.getCustomerId());

        // Validação dos produtos e estoque
        order.getItems().forEach(item -> {
            productGateway.getProductById(item.getProductId());
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
