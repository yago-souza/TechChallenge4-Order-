package br.com.fiap.postech.orders.infrastructure.http;

import br.com.fiap.postech.orders.domain.models.Customer;
import br.com.fiap.postech.orders.domain.gateways.CustomerGateway;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.UUID;

@Component
public class RestTemplateCustomerGateway implements CustomerGateway {

    private final RestTemplate restTemplate;
    private final String clientServiceUrl = "http://localhost:8081/clients";

    public RestTemplateCustomerGateway(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public Customer getCustomerById(UUID clientId) {
        String url = clientServiceUrl + "/" + clientId;
        return restTemplate.getForObject(url, Customer.class);
    }
}
