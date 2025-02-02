package br.com.fiap.postech.orders.infrastructure;

import br.com.fiap.postech.orders.domain.Client;
import br.com.fiap.postech.orders.gateway.ClientGateway;
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
