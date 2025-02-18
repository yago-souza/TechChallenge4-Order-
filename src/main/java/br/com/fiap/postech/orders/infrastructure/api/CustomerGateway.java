package br.com.fiap.postech.orders.infrastructure.api;

import br.com.fiap.postech.orders.infrastructure.api.models.Customer;

import java.util.UUID;

public interface CustomerGateway {
    Customer getCustomerById(UUID customerId);
}
