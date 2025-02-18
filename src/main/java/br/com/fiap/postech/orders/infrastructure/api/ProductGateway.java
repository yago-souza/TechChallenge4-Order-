package br.com.fiap.postech.orders.infrastructure.api;

import br.com.fiap.postech.orders.infrastructure.api.models.Product;

import java.util.UUID;

public interface ProductGateway {
    Product getProductById(UUID productId);
    boolean isInStock(UUID productId, int quantity);
}
