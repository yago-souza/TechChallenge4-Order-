package br.com.fiap.postech.orders.infrastructure.API;

import br.com.fiap.postech.orders.infrastructure.API.models.Customer;

import java.util.UUID;

public interface CustomerGateway {
    Customer getCustomerById(UUID customerId);
}
