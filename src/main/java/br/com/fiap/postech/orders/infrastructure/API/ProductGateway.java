package br.com.fiap.postech.orders.infrastructure.API;

import br.com.fiap.postech.orders.infrastructure.API.models.Product;

import java.util.UUID;

public interface ProductGateway {
    Product getProductById(UUID productId);
    boolean isInStock(UUID productId, int quantity);
}
