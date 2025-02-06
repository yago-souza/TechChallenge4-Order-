package br.com.fiap.postech.orders.services;

import br.com.fiap.postech.orders.domain.gateways.ProductGateway;
import br.com.fiap.postech.orders.domain.models.Product;
import br.com.fiap.postech.orders.infrastructure.exception.InsufficientStockException;
import br.com.fiap.postech.orders.infrastructure.exception.ProductNotFoundException;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ProductService {

    private final ProductGateway productGateway;

    public ProductService(ProductGateway productGateway) {
        this.productGateway = productGateway;
    }

    public void validateProduct(UUID productId, int quantity) {
        Product product = productGateway.getProductById(productId);
        if (product == null) {
            throw new ProductNotFoundException("Produto não encontrado: " + productId);
        }
        if (!productGateway.isInStock(productId, quantity)) {
            throw new InsufficientStockException("Estoque insuficiente para o produto: " + productId);
        }
    }

    public boolean existsById(UUID productId) {
        try {
            Product product = productGateway.getProductById(productId);
            return product != null; // Retorna true se o produto existir
        } catch (Exception e) {
            return false; // Retorna false se ocorrer um erro (ex: produto não encontrado)
        }
    }

    public boolean isInStock(UUID productId, int quantity) {
        try {
            return productGateway.isInStock(productId, quantity); // Retorna true se houver estoque suficiente
        } catch (Exception e) {
            return false; // Retorna false se ocorrer um erro (ex: produto não encontrado)
        }
    }
}