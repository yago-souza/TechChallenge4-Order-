package br.com.fiap.postech.orders.application.usecases;

import br.com.fiap.postech.orders.domain.enums.OrderStatus;
import br.com.fiap.postech.orders.domain.enums.PaymentMethod;
import br.com.fiap.postech.orders.infrastructure.API.models.Customer;
import br.com.fiap.postech.orders.infrastructure.API.models.Product;
import br.com.fiap.postech.orders.domain.entities.Order;
import br.com.fiap.postech.orders.infrastructure.API.CustomerGateway;
import br.com.fiap.postech.orders.infrastructure.API.ProductGateway;
import br.com.fiap.postech.orders.infrastructure.exception.*;
import br.com.fiap.postech.orders.infrastructure.gateway.impl.OrderRepositoryGatewayImpl;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;

@Service
public class CreateOrderUsecase {

    private final OrderRepositoryGatewayImpl orderRepositoryGateway;
    private final CustomerGateway customerGateway;
    private final ProductGateway productGateway;

    public CreateOrderUsecase(OrderRepositoryGatewayImpl orderRepositoryGateway, CustomerGateway customerGateway, ProductGateway productGateway) {
        this.orderRepositoryGateway = orderRepositoryGateway;
        this.customerGateway = customerGateway;
        this.productGateway = productGateway;
    }

    @Transactional
    public Order execute(Order order) {
        validateOrder(order);

        order.setStatus(OrderStatus.OPEN);
        order.setCreatedAt(LocalDateTime.now());
        order.setUpdatedAt(LocalDateTime.now());

        return orderRepositoryGateway.save(order);
    }

    private void validateOrder(Order order) {
        if (order.getCustomerId() == null) {
            throw new NoCustomerException("O pedido deve ter um cliente associado.");
        }
        Customer customer = customerGateway.getCustomerById(order.getCustomerId());
        if (customer == null) {
            throw new CustomerNotFoundException("Cliente não encontrado para o ID: " + order.getCustomerId());
        }

        if (!StringUtils.hasText(order.getDeliveryAddress())) {
            throw new InvalidAddressException("O endereço de entrega não pode ser nulo ou vazio.");
        }

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
    }
}
