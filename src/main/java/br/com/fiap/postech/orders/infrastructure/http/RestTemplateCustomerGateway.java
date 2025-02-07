package br.com.fiap.postech.orders.infrastructure.http;

import br.com.fiap.postech.orders.domain.gateways.CustomerGateway;
import br.com.fiap.postech.orders.domain.models.Customer;
import br.com.fiap.postech.orders.infrastructure.exception.CustomerNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.UUID;

@Component
public class RestTemplateCustomerGateway implements CustomerGateway {

    private final RestTemplate restTemplate;
    private final String customerServiceUrl = "http://localhost:8081/clients";

    public RestTemplateCustomerGateway(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public Customer getCustomerById(UUID clientId) {
        String url = customerServiceUrl + "/" + clientId;
        try {
            return restTemplate.getForObject(url, Customer.class);
        } catch (HttpClientErrorException.NotFound ex) {
            throw new CustomerNotFoundException("Cliente não encontrado: " + clientId);
        }
    }
}