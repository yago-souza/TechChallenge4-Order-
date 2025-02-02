package br.com.fiap.postech.orders.service;

import br.com.fiap.postech.orders.domain.Client;
import br.com.fiap.postech.orders.exeption.ClientNotFoundException;
import br.com.fiap.postech.orders.gateway.ClientGateway;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ClientService {

    private final ClientGateway clientGateway;

    public ClientService(ClientGateway clientGateway) {
        this.clientGateway = clientGateway;
    }

    public void validateClient(UUID clientId) {
        Client client = clientGateway.getClientById(clientId);
        if (client == null) {
            throw new ClientNotFoundException("Cliente não encontrado: " + clientId);
        }
    }

    public boolean existsById(UUID clientId) {
        try {
            Client client = clientGateway.getClientById(clientId);
            return client != null; // Retorna true se o cliente existir
        } catch (Exception e) {
            return false; // Retorna false se ocorrer um erro (ex: cliente não encontrado)
        }
    }
}
