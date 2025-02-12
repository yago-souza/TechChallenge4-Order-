package br.com.fiap.postech.orders.services;

import br.com.fiap.postech.orders.domain.models.Customer;
import br.com.fiap.postech.orders.infrastructure.exception.CustomerNotFoundException;
import br.com.fiap.postech.orders.domain.gateways.CustomerGateway;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class CustomerServiceTest {

    @Mock
    private CustomerGateway customerGateway;

    @InjectMocks
    private CustomerService customerService;

    @Test
    public void testValidateClient_ClientExists() {
        // Arrange
        UUID clientId = UUID.randomUUID();

        Customer customer = new Customer(clientId, "Cliente Teste", "cliente@teste.com", "Rua Teste, 123");
        when(customerGateway.getCustomerById(clientId)).thenReturn(customer);

        // Act & Assert
        assertDoesNotThrow(() -> customerService.validateClient(clientId));
    }

    @Test
    public void testValidateClient_ClientNotFound() {
        // Arrange
        UUID clientId = UUID.randomUUID();

        when(customerGateway.getCustomerById(clientId)).thenReturn(null);

        // Act & Assert
        assertThrows(CustomerNotFoundException.class, () -> customerService.validateClient(clientId));
    }

    @Test
    public void testExistsById_ClientExists() {
        // Arrange
        UUID clientId = UUID.randomUUID();

        Customer customer = new Customer(clientId, "Cliente Teste", "cliente@teste.com", "Rua Teste, 123");
        when(customerGateway.getCustomerById(clientId)).thenReturn(customer);

        // Act
        boolean exists = customerService.existsById(clientId);

        // Assert
        assertTrue(exists);
    }

    @Test
    public void testExistsById_ClientNotFound() {
        // Arrange
        UUID clientId = UUID.randomUUID();

        when(customerGateway.getCustomerById(clientId)).thenReturn(null);

        // Act
        boolean exists = customerService.existsById(clientId);

        // Assert
        assertFalse(exists);
    }
}
