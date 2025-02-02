package br.com.fiap.postech.orders.gateway;

import br.com.fiap.postech.orders.domain.Client;

import java.util.UUID;

public interface ClientGateway {
    Client getClientById(UUID clientId);
}
