package br.com.fiap.postech.orders.infrastructure.api;

import br.com.fiap.postech.orders.infrastructure.api.models.Customer;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.UUID;

@Component
public class RestTemplateCustomerGateway implements CustomerGateway {

    private final RestTemplate restTemplate;
    private static final String CLIENT_SERVICE_URL = "http://localhost:8081/clients";

    public RestTemplateCustomerGateway(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public Customer getCustomerById(UUID customerId) {
        String url = CLIENT_SERVICE_URL + "/" + customerId;
        return restTemplate.getForObject(url, Customer.class);
    }
}
