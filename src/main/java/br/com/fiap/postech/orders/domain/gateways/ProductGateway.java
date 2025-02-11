package br.com.fiap.postech.orders.domain.gateways;

import br.com.fiap.postech.orders.domain.models.Product;

import java.util.UUID;

public interface ProductGateway {
    Product getProductById(UUID productId);
    boolean isInStock(UUID productId, int quantity);
}
