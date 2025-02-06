package br.com.fiap.postech.orders.infrastructure.http;

import br.com.fiap.postech.orders.domain.gateways.ClientGateway;
import br.com.fiap.postech.orders.domain.models.Client;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.UUID;

@Component
public class RestTemplateClientGateway implements ClientGateway {

    private final RestTemplate restTemplate;
    private final String clientServiceUrl = "http://localhost:8081/clients";

    public RestTemplateClientGateway(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public Client getClientById(UUID clientId) {
        String url = clientServiceUrl + "/" + clientId;
        return restTemplate.getForObject(url, Client.class);
    }
}
