package br.com.fiap.postech.orders.domain.gateways;

import br.com.fiap.postech.orders.domain.models.Customer;

import java.util.UUID;

public interface CustomerGateway {
    Customer getCustomerById(UUID clientId);
}
