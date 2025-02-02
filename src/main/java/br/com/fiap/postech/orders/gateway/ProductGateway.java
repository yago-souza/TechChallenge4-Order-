package br.com.fiap.postech.orders.gateway;

import br.com.fiap.postech.orders.domain.Product;

import java.util.UUID;

public interface ProductGateway {
    Product getProductById(UUID productId);
    boolean isInStock(UUID productId, int quantity);
}
