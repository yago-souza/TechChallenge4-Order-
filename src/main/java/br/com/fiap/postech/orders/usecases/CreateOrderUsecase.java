package br.com.fiap.postech.orders.usecases;

import br.com.fiap.postech.orders.domain.enums.OrderStatus;
import br.com.fiap.postech.orders.domain.models.Customer;
import br.com.fiap.postech.orders.domain.models.Product;
import br.com.fiap.postech.orders.domain.entities.Order;
import br.com.fiap.postech.orders.domain.gateways.CustomerGateway;
import br.com.fiap.postech.orders.domain.gateways.ProductGateway;
import br.com.fiap.postech.orders.infrastructure.exception.*;
import br.com.fiap.postech.orders.infrastructure.persistence.repositories.OrderRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class CreateOrderUsecase {

    private final OrderRepository orderRepository;
    private final CustomerGateway customerGateway;
    private final ProductGateway productGateway;

    public CreateOrderUsecase(OrderRepository orderRepository, CustomerGateway customerGateway, ProductGateway productGateway) {
        this.orderRepository = orderRepository;
        this.customerGateway = customerGateway;
        this.productGateway = productGateway;
    }

    @Transactional
    public Order execute(Order order) {
        // Validar se o cliente existe
        if (order.getCustomerId() == null) {
            throw new NoCustomerException("O pedido deve ter um cliente associado.");
        }
        Customer customer = customerGateway.getCustomerById(order.getCustomerId());
        if (customer == null) {
            throw new CustomerNotFoundException("Cliente não encontrado para o ID: " + order.getCustomerId());
        }

        // Validar produtos e estoque
        if (order.getItems().isEmpty()) {
            throw new NoItemException("Pedido vazio.");
        }
        order.getItems().forEach(item -> {
            if (item.getQuantity() <= 0) {
                throw new InvalidQuantityException("A quantidade deve ser maior que zero para o produto: " + item.getProductId());
            }
            if (item.getTotalPrice() < 0) {
                throw new InvalidPriceException("O valor total do item não pode ser negativo para o produto: " + item.getProductId());
            }

            Product product = productGateway.getProductById(item.getProductId());
            if (product == null) {
                throw new ProductNotFoundException("Produto não encontrado: " + item.getProductId());
            }
            if (!productGateway.isInStock(item.getProductId(), item.getQuantity())) {
                throw new InsufficientStockException("Estoque insuficiente para o produto: " + item.getProductId());
            }
        });

        if (order.getTotalAmount() < 0) {
            throw new InvalidTotalAmountException("O valor total do pedido não pode ser negativo.");
        }

        order.setStatus(OrderStatus.OPEN);
        order.setCreatedAt(LocalDateTime.now());
        order.setUpdatedAt(LocalDateTime.now());

        return orderRepository.save(order);
    }
}
