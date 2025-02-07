package br.com.fiap.postech.orders.services;

import br.com.fiap.postech.orders.domain.gateways.CustomerGateway;
import br.com.fiap.postech.orders.domain.models.Customer;
import br.com.fiap.postech.orders.infrastructure.exception.CustomerNotFoundException;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class CustomerService {

    private final CustomerGateway customerGateway;

    public CustomerService(CustomerGateway customerGateway) {
        this.customerGateway = customerGateway;
    }

    public void validateClient(UUID clientId) {
        Customer customer = customerGateway.getCustomerById(clientId);
        if (customer == null) {
            throw new CustomerNotFoundException("Cliente não encontrado: " + clientId);
        }
    }

    public boolean existsById(UUID clientId) {
        try {
            Customer customer = customerGateway.getCustomerById(clientId);
            return customer != null; // Retorna true se o cliente existir
        } catch (Exception e) {
            return false; // Retorna false se ocorrer um erro (ex: cliente não encontrado)
        }
    }
}
