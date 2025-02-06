package br.com.fiap.postech.orders.infrastructure.http;

import br.com.fiap.postech.orders.domain.gateways.ProductGateway;
import br.com.fiap.postech.orders.domain.models.Product;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.UUID;

@Component
public class RestTemplateProductGateway implements ProductGateway {

    private final RestTemplate restTemplate;
    private final String productServiceUrl = "http://localhost:8082/products"; // URL do microsserviço de produtos

    public RestTemplateProductGateway(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public Product getProductById(UUID productId) {
        String url = productServiceUrl + "/" + productId;
        return restTemplate.getForObject(url, Product.class); // Faz uma requisição GET
    }

    @Override
    public boolean isInStock(UUID productId, int quantity) {
        String url = productServiceUrl + "/" + productId + "/stock?quantity=" + quantity;
        return restTemplate.getForObject(url, Boolean.class); // Faz uma requisição GET
    }
}