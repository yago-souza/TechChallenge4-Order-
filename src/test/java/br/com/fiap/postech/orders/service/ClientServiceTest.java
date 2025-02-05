package br.com.fiap.postech.orders.service;

import br.com.fiap.postech.orders.domain.Client;
import br.com.fiap.postech.orders.exeption.ClientNotFoundException;
import br.com.fiap.postech.orders.gateway.ClientGateway;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class ClientServiceTest {

    @Mock
    private ClientGateway clientGateway;

    @InjectMocks
    private ClientService clientService;

    @Test
    public void testValidateClient_ClientExists() {
        // Arrange
        UUID clientId = UUID.randomUUID();

        Client client = new Client(clientId, "Cliente Teste", "cliente@teste.com", "Rua Teste, 123");
        when(clientGateway.getClientById(clientId)).thenReturn(client);

        // Act & Assert
        assertDoesNotThrow(() -> clientService.validateClient(clientId));
    }

    @Test
    public void testValidateClient_ClientNotFound() {
        // Arrange
        UUID clientId = UUID.randomUUID();

        when(clientGateway.getClientById(clientId)).thenReturn(null);

        // Act & Assert
        assertThrows(ClientNotFoundException.class, () -> clientService.validateClient(clientId));
    }

    @Test
    public void testExistsById_ClientExists() {
        // Arrange
        UUID clientId = UUID.randomUUID();

        Client client = new Client(clientId, "Cliente Teste", "cliente@teste.com", "Rua Teste, 123");
        when(clientGateway.getClientById(clientId)).thenReturn(client);

        // Act
        boolean exists = clientService.existsById(clientId);

        // Assert
        assertTrue(exists);
    }

    @Test
    public void testExistsById_ClientNotFound() {
        // Arrange
        UUID clientId = UUID.randomUUID();

        when(clientGateway.getClientById(clientId)).thenReturn(null);

        // Act
        boolean exists = clientService.existsById(clientId);

        // Assert
        assertFalse(exists);
    }
}
