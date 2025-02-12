package br.com.fiap.postech.orders.services;

import br.com.fiap.postech.orders.domain.models.Customer;
import br.com.fiap.postech.orders.infrastructure.exception.CustomerNotFoundException;
import br.com.fiap.postech.orders.domain.gateways.CustomerGateway;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class CustomerService {

    private final CustomerGateway customerGateway;

    public CustomerService(CustomerGateway customerGateway) {
        this.customerGateway = customerGateway;
    }

    public Customer validateClient(UUID clientId) {
        Customer customer = customerGateway.getCustomerById(clientId);
        if (customer == null) {
            throw new CustomerNotFoundException("Cliente não encontrado: " + clientId);
        }
        return customer;
    }

    public boolean existsById(UUID clientId) {
        try {
            Customer customer = customerGateway.getCustomerById(clientId);
            return customer != null;
        } catch (Exception e) {
            return false;
        }
    }
}
