package br.com.fiap.postech.orders.service;

import br.com.fiap.postech.orders.domain.Product;
import br.com.fiap.postech.orders.exeption.InsufficientStockException;
import br.com.fiap.postech.orders.exeption.ProductNotFoundException;
import br.com.fiap.postech.orders.gateway.ProductGateway;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {

    @Mock
    private ProductGateway productGateway;

    @InjectMocks
    private ProductService productService;

    @Test
    public void testValidateProduct_ProductExistsAndInStock() {
        // Arrange
        UUID productId = UUID.randomUUID();
        int quantity = 2;

        Product product = new Product(productId, "Produto Teste", "Descrição Teste", 50.0, 10);
        when(productGateway.getProductById(productId)).thenReturn(product);
        when(productGateway.isInStock(productId, quantity)).thenReturn(true);

        // Act & Assert
        assertDoesNotThrow(() -> productService.validateProduct(productId, quantity));
    }

    @Test
    public void testValidateProduct_ProductNotFound() {
        // Arrange
        UUID productId = UUID.randomUUID();
        int quantity = 2;

        when(productGateway.getProductById(productId)).thenReturn(null);

        // Act & Assert
        assertThrows(ProductNotFoundException.class, () -> productService.validateProduct(productId, quantity));
    }

    @Test
    public void testValidateProduct_InsufficientStock() {
        // Arrange
        UUID productId = UUID.randomUUID();
        int quantity = 2;

        Product product = new Product(productId, "Produto Teste", "Descrição Teste", 50.0, 10);
        when(productGateway.getProductById(productId)).thenReturn(product);
        when(productGateway.isInStock(productId, quantity)).thenReturn(false);

        // Act & Assert
        assertThrows(InsufficientStockException.class, () -> productService.validateProduct(productId, quantity));
    }

    @Test
    public void testExistsById_ProductExists() {
        // Arrange
        UUID productId = UUID.randomUUID();

        Product product = new Product(productId, "Produto Teste", "Descrição Teste", 50.0, 10);
        when(productGateway.getProductById(productId)).thenReturn(product);

        // Act
        boolean exists = productService.existsById(productId);

        // Assert
        assertTrue(exists);
    }

    @Test
    public void testExistsById_ProductNotFound() {
        // Arrange
        UUID productId = UUID.randomUUID();

        when(productGateway.getProductById(productId)).thenReturn(null);

        // Act
        boolean exists = productService.existsById(productId);

        // Assert
        assertFalse(exists);
    }

    @Test
    public void testIsInStock_StockSufficient() {
        // Arrange
        UUID productId = UUID.randomUUID();
        int quantity = 2;

        when(productGateway.isInStock(productId, quantity)).thenReturn(true);

        // Act
        boolean inStock = productService.isInStock(productId, quantity);

        // Assert
        assertTrue(inStock);
    }

    @Test
    public void testIsInStock_StockInsufficient() {
        // Arrange
        UUID productId = UUID.randomUUID();
        int quantity = 2;

        when(productGateway.isInStock(productId, quantity)).thenReturn(false);

        // Act
        boolean inStock = productService.isInStock(productId, quantity);

        // Assert
        assertFalse(inStock);
    }
}