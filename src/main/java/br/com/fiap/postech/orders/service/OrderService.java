package br.com.fiap.postech.orders.service;

import br.com.fiap.postech.orders.domain.Order;
import br.com.fiap.postech.orders.exeption.ClientNotFoundException;
import br.com.fiap.postech.orders.exeption.InsufficientStockException;
import br.com.fiap.postech.orders.exeption.ProductNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class OrderService {

    private final ClientService clientService;
    private final ProductService productService;

    public OrderService(ClientService clientService, ProductService productService) {
        this.clientService = clientService;
        this.productService = productService;
    }

    public void validateOrder(Order order) {
        // Valida se o cliente existe
        if (!clientService.existsById(order.getClientId())) {
            throw new ClientNotFoundException("Cliente não encontrado: " + order.getClientId());
        }

        // Valida se os produtos existem e têm estoque suficiente
        order.getItems().forEach(item -> {
            if (!productService.existsById(item.getProductId())) {
                throw new ProductNotFoundException("Produto não encontrado: " + item.getProductId());
            }
            if (!productService.isInStock(item.getProductId(), item.getQuantity())) {
                throw new InsufficientStockException("Estoque insuficiente para o produto: " + item.getProductId());
            }
        });
    }
}