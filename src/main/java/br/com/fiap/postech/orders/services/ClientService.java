package br.com.fiap.postech.orders.services;

import br.com.fiap.postech.orders.domain.models.Client;
import br.com.fiap.postech.orders.infrastructure.exception.ClientNotFoundException;
import br.com.fiap.postech.orders.domain.gateways.ClientGateway;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ClientService {

    private final ClientGateway clientGateway;

    public ClientService(ClientGateway clientGateway) {
        this.clientGateway = clientGateway;
    }

    public Client validateClient(UUID clientId) {
        Client client = clientGateway.getClientById(clientId);
        if (client == null) {
            throw new ClientNotFoundException("Cliente não encontrado: " + clientId);
        }
        return client;
    }

    public boolean existsById(UUID clientId) {
        try {
            Client client = clientGateway.getClientById(clientId);
            return client != null;
        } catch (Exception e) {
            return false;
        }
    }
}
