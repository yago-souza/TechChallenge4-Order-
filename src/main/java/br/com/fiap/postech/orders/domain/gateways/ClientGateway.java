package br.com.fiap.postech.orders.domain.gateways;

import br.com.fiap.postech.orders.domain.models.Client;

import java.util.UUID;

public interface ClientGateway {
    Client getClientById(UUID clientId);
}
